package com.project.sns.service;

import com.project.sns.controller.NotificationSocketController;
import com.project.sns.dto.CommentRequestDto;
import com.project.sns.dto.CommentResponseDto;
import com.project.sns.entity.Comment;
import com.project.sns.entity.CommentLike;
import com.project.sns.entity.Notification;
import com.project.sns.enums.NotificationType;
import com.project.sns.repository.CommentLikeRepository;
import com.project.sns.repository.CommentRepository;
import com.project.sns.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 댓글 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationSocketController notificationSocketController;
  private final CommentLikeRepository commentLikeRepository;

  /**
   * 댓글 또는 대댓글을 생성합니다.
   *
   * @param postId 댓글을 달 게시글 ID
   * @param userId 댓글 작성자 ID
   * @param dto 댓글 요청 데이터
   */
  public void createComment(Long postId, Long userId, CommentRequestDto dto) {
    Comment comment = new Comment();
    comment.setPostId(postId);
    comment.setUserId(userId);
    comment.setParentCommentId(dto.getParentCommentId());
    comment.setContent(dto.getContent());
    comment.setCreatedAt(LocalDateTime.now());
    comment.setUpdatedAt(LocalDateTime.now());

    // 댓글 저장
    commentRepository.save(comment);

    // WebSocket 실시간 알림 전송
    Long postWriterId = 2L; // TODO: 실제 게시글 작성자의 ID로 변경 필요
    notificationSocketController.sendNotification(
        postWriterId,
        "회원님의 게시글에 댓글이 달렸습니다."
    );

    // 🟡 댓글 작성 시 알림 생성 로직 (NotificationService 분리 가능)
    Notification notification = new Notification();
    notification.setUserId(2L); // TODO: 나중엔 실제 게시글 작성자의 ID로 변경
    notification.setTitle("새로운 댓글이 달렸습니다");
    notification.setMessage(dto.getContent());
    notification.setNotificationType(NotificationType.COMMENT);
    notification.setIsRead(false);
    notification.setCreatedAt(LocalDateTime.now());

    notificationRepository.save(notification);
  }

  /**
   * 게시글에 해당하는 모든 댓글을 조회합니다.
   *
   * @param postId 게시글 ID
   * @return 댓글 응답 리스트
   */
  public List<CommentResponseDto> getComments(Long postId) {
    return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
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

  //댓글 좋아요 (토글)
  @Transactional
  public Map<String, Object> toggleCommentLike(Long commentId, Long userId) {
    Optional<CommentLike> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

    boolean isLiked;
    if (existingLike.isPresent()) {
      // 좋아요 취소
      commentLikeRepository.delete(existingLike.get());
      isLiked = false;
    } else {
      // 좋아요 등록
      CommentLike like = CommentLike.builder()
              .commentId(commentId)
              .userId(userId)
              .build();
      commentLikeRepository.save(like);
      isLiked = true;
    }

    Long likeCount = commentLikeRepository.countByCommentId(commentId);

    return Map.of(
            "isLiked", isLiked,
            "likeCount", likeCount
    );
  }
}

