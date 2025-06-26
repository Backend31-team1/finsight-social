// CommentController.java
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * POST /api/sns/posts/{postId}/comments : 댓글 또는 대댓글 등록
   */
  @PostMapping
  public ResponseEntity<String> createComment(
      @PathVariable Long postId,
      @RequestBody CommentRequestDto request
  ) {
    // 로그인 연동 전이라 임시 userId 사용
    commentService.createComment(postId, 1L, request);
    return ResponseEntity.ok("댓글 작성 완료");
  }

  /**
   * GET /api/sns/posts/{postId}/comments : 댓글 목록 조회
   */
  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
    return ResponseEntity.ok(commentService.getComments(postId));
  }

  /**
   * POST /api/sns/posts/{postId}/comments/{commentId}/like : 좋아요 토글
   */
  @PostMapping("/{commentId}/like")
  public ResponseEntity<Map<String, Object>> toggleCommentLike(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserVo user
  ) {
    return ResponseEntity.ok(commentService.toggleCommentLike(commentId, user.getId()));
  }
}
