package com.project.sns.controller;

import com.project.sns.dto.CommentRequestDto;
import com.project.sns.dto.CommentResponseDto;
import com.project.sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 API 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  /**
   * 댓글 또는 대댓글을 등록합니다.
   *
   * @param postId 게시글 ID
   * @param request 댓글 요청 데이터
   * @return 작성 완료 메시지
   */
  @PostMapping
  public ResponseEntity<String> createComment(@PathVariable Long postId,
      @RequestBody CommentRequestDto request) {
    Long mockUserId = 1L; // 로그인 연동 전이므로 임시 고정
    commentService.createComment(postId, mockUserId, request);
    return ResponseEntity.ok("댓글 작성 완료");
  }

  /**
   * 특정 게시글의 댓글/대댓글 목록을 조회합니다.
   *
   * @param postId 게시글 ID
   * @return 댓글 응답 리스트
   */
  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
    return ResponseEntity.ok(commentService.getComments(postId));
  }
}