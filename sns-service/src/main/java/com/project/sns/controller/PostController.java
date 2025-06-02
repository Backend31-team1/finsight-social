package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.dto.PostRequest;
import com.project.sns.dto.PostResponse;
import com.project.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  /**
   * 게시글 생성 POST /posts
   */
  @PostMapping
  public ResponseEntity<PostResponse> createPost(
      @RequestBody PostRequest request,
      @AuthenticationPrincipal UserVo userVo
  ) {
    // JWT 인증을 통해 SecurityContext 에 저장된 UserVo 로 userId 취득
    Long userId = userVo.getId();
    request.setUserId(userId);

    PostResponse response = postService.createPost(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 게시글 하나씩 조회 (조회수 증가 및 인기집계 반영)
   */
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponse> getPost(
      @PathVariable Long postId
  ) {
    PostResponse response = postService.getPost(postId);
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 페이징 조회 (최신순)
   */
  @GetMapping
  public ResponseEntity<Page<PostResponse>> getAllPosts(
      Pageable pageable
  ) {
    Page<PostResponse> page = postService.getAllPosts(pageable);
    return ResponseEntity.ok(page);
  }

  /**
   * 게시글 수정
   */
  @PutMapping("/{postId}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable Long postId,
      @RequestBody PostRequest request,
      @AuthenticationPrincipal UserVo userVo
  ) {
    Long userId = userVo.getId();
    request.setUserId(userId);

    PostResponse response = postService.updatePost(postId, request);
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 삭제 (작성자만 삭제 가능)
   */
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserVo userVo
  ) {
    Long userId = userVo.getId();
    postService.deletePost(postId, userId);
    return ResponseEntity.noContent().build();
  }


  /**
   * 인기 게시글 조회 (가중치 기반, 페이징처리)
   */
  @GetMapping("/trending")
  public ResponseEntity<Page<PostResponse>> getTrendingPosts(
      Pageable pageable
  ) {
    Page<PostResponse> page = postService.getTrendingPosts(pageable);
    return ResponseEntity.ok(page);
  }
}
