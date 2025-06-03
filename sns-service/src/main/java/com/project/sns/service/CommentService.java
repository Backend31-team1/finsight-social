package com.project.sns.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.sns.application.PostApplication;
import com.project.sns.controller.NotificationSocketController;
import com.project.sns.dto.CommentRequestDto;
import com.project.sns.dto.CommentResponseDto;
import com.project.sns.entity.Comment;
import com.project.sns.entity.CommentLike;
import com.project.sns.entity.Notification;
import com.project.sns.entity.Post;
import com.project.sns.enums.NotificationType;
import com.project.sns.repository.CommentLikeRepository;
import com.project.sns.repository.CommentRepository;
import com.project.sns.repository.NotificationRepository;
import com.project.sns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 댓글 관련 비즈니스 로직을 담당하는 서비스
 *
 * 댓글/대댓글의 생성, 조회, 삭제 및 좋아요 기능을 처리합니다.
 * - 댓글 작성 시 실시간 알림 발송
 * - 댓글 삭제 시 계층형 구조(대댓글) 고려
 * - 인기게시물 점수 실시간 반영
 */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationSocketController notificationSocketController;
  private final CommentLikeRepository commentLikeRepository;
  private final PostApplication postApplication;
  private final PostRepository postRepository;

  /**
   * 댓글 또는 대댓글을 생성합니다.
   *
   * parentCommentId가 있으면 대댓글, 없으면 일반 댓글로 처리됩니다.
   * 작성 후 게시글 작성자에게 실시간 알림을 발송합니다.
   *
   * @param postId 댓글을 달 게시글 ID
   * @param userId 댓글 작성자 ID
   * @param dto 댓글 요청 데이터
   */
  @Transactional
  public void createComment(Long postId, Long userId, CommentRequestDto dto) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUserId(userId);
    comment.setParentCommentId(dto.getParentCommentId());
    comment.setContent(dto.getContent());
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    commentRepository.save(comment);

    // 인기게시물 점수 갱신
    if (dto.getParentCommentId() == null) {
      // 일반 댓글
      postApplication.recordComment(postId);
    } else {
      // 대댓글
      postApplication.recordUnderComment(postId);
    }

    // 게시글 작성자에게 실시간 알림 발송
    if (!post.getUserId().equals(userId)) { // 자신의 게시글에 댓글 단 경우 제외
      String message = dto.getParentCommentId() == null ?
          "회원님의 게시글에 댓글이 달렸습니다." :
          "회원님의 게시글에 대댓글이 달렸습니다.";

      notificationSocketController.sendNotification(post.getUserId(), message);

      // 알림 엔티티 저장
      Notification notification = new Notification();
      notification.setUserId(post.getUserId());
      notification.setTitle("새로운 댓글");
      notification.setMessage(message);
      notification.setNotificationType(NotificationType.COMMENT);
      notification.setIsRead(false);
      notification.setCreatedAt(LocalDateTime.now());

      notificationRepository.save(notification);
    }
  }

  /**
   * 게시글에 해당하는 모든 댓글을 조회합니다.
   *
   * 생성시간 기준 오름차순으로 정렬하여 반환합니다.
   *
   * @param postId 게시글 ID
   * @return 댓글 응답 리스트
   */
  @Transactional(readOnly = true)
  public List<CommentResponseDto> getComments(Long postId) {
    return commentRepository.findByPost_PostIdOrderByCreatedAtAsc(postId)
        .stream()
        .map(comment -> {
          CommentResponseDto dto = new CommentResponseDto();
          dto.setCommentId(comment.getCommentId());
          dto.setParentCommentId(comment.getParentCommentId());
          dto.setContent(comment.getContent());
          dto.setCreatedAt(comment.getCreatedAt());
          return dto;
        })
        .toList();
  }

  /**
   * 댓글 좋아요를 토글합니다.
   *
   * 이미 좋아요가 있으면 취소, 없으면 추가합니다.
   *
   * @param commentId 댓글 ID
   * @param userId 사용자 ID
   * @return 좋아요 상태와 총 좋아요 수
   */
  @Transactional
  public Map<String, Object> toggleCommentLike(Long commentId, Long userId) {
    Optional<CommentLike> existingLike = commentLikeRepository.findByComment_CommentIdAndUserId(commentId, userId);

    boolean isLiked;
    if (existingLike.isPresent()) {
      // 좋아요 취소
      commentLikeRepository.delete(existingLike.get());
      isLiked = false;
    } else {
      // 댓글 존재 확인
      Comment comment = commentRepository.findById(commentId)
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

      // 좋아요 등록
      CommentLike like = CommentLike.builder()
          .comment(comment)
          .userId(userId)
          .build();
      commentLikeRepository.save(like);
      isLiked = true;
    }

    Long likeCount = commentLikeRepository.countByComment_CommentId(commentId);

    return Map.of(
        "isLiked", isLiked,
        "likeCount", likeCount
    );
  }

  /**
   * 댓글을 삭제합니다.
   *
   * 일반 댓글 삭제 시 해당 댓글의 모든 대댓글도 함께 삭제됩니다.
   * 작성자 본인만 삭제할 수 있습니다.
   *
   * @param commentId 삭제할 댓글 ID
   * @param userId 요청자(댓글 작성자) ID
   * @throws CustomException 권한이 없거나 댓글이 존재하지 않을 경우
   */
  @Transactional
  public void deleteComment(Long commentId, Long userId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT));

    // 작성자 검증
    if (!comment.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    Long postId = comment.getPost().getPostId();
    boolean isParentComment = comment.getParentCommentId() == null;

    if (isParentComment) {
      // 일반 댓글 삭제 시: 해당 댓글의 모든 대댓글도 함께 삭제
      List<Comment> replies = commentRepository.findByParentCommentId(commentId);

      // 대댓글들 먼저 삭제 (Cascade로 CommentLike도 함께 삭제됨)
      commentRepository.deleteAll(replies);

      // 인기게시물 점수에서 대댓글 수만큼 차감
      for (int i = 0; i < replies.size(); i++) {
        postApplication.recordUnderCommentRemoval(postId);
      }
    } else {
      // 대댓글 삭제 시: underComment 점수 차감
      postApplication.recordUnderCommentRemoval(postId);
    }

    // 원본 댓글 삭제 (Cascade로 CommentLike도 함께 삭제됨)
    commentRepository.delete(comment);

    // 인기게시물 점수에서 해당 댓글 차감 (일반 댓글인 경우만)
    if (isParentComment) {
      postApplication.recordCommentRemoval(postId);
    }
  }
}