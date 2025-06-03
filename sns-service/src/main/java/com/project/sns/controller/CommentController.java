package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.dto.CommentRequestDto;
import com.project.sns.dto.CommentResponseDto;
import com.project.sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 댓글 API 요청을 처리하는 컨트롤러
 *
 * 게시글의 댓글과 대댓글 관련 모든 기능을 제공합니다.
 * - 댓글/대댓글 작성 및 조회
 * - 댓글 좋아요 토글
 * - 댓글 삭제 (대댓글 포함)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 또는 대댓글을 등록합니다.
   *
   * parentCommentId가 포함되면 대댓글, 없으면 일반 댓글로 처리됩니다.
   *
   * @param postId 게시글 ID
   * @param request 댓글 요청 데이터
   * @param user 인증된 사용자 정보
   * @return 작성 완료 메시지
   */
  @PostMapping
  public ResponseEntity<String> createComment(
      @PathVariable Long postId,
      @RequestBody CommentRequestDto request,
      @AuthenticationPrincipal UserVo user
  ) {
    commentService.createComment(postId, user.getId(), request);
    return ResponseEntity.ok("댓글 작성 완료");
  }

  /**
   * 특정 게시글의 댓글/대댓글 목록을 조회합니다.
   *
   * 생성시간 기준 오름차순으로 정렬하여 반환합니다.
   *
   * @param postId 게시글 ID
   * @return 댓글 응답 리스트
   */
  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
    return ResponseEntity.ok(commentService.getComments(postId));
  }

  /**
   * 댓글 좋아요를 토글합니다.
   *
   * 이미 좋아요가 있으면 취소, 없으면 추가합니다.
   *
   * @param commentId 댓글 ID
   * @param user 인증된 사용자 정보
   * @return 좋아요 상태와 총 좋아요 수
   */
  @PostMapping("/{commentId}/like")
  public ResponseEntity<Map<String, Object>> toggleCommentLike(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserVo user
  ) {
    Map<String, Object> result = commentService.toggleCommentLike(commentId, user.getId());
    return ResponseEntity.ok(result);
  }

  /**
   * 댓글을 삭제합니다.
   *
   * 일반 댓글 삭제 시 해당 댓글의 모든 대댓글도 함께 삭제됩니다.
   * 작성자 본인만 삭제할 수 있습니다.
   *
   * @param commentId 삭제할 댓글 ID
   * @param user 인증된 사용자 정보
   * @return 성공 시 204 No Content
   */
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserVo user
  ) {
    commentService.deleteComment(commentId, user.getId());
    return ResponseEntity.noContent().build();
  }
}