// PostController.java
package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.dto.PostRequest;
import com.project.sns.dto.PostResponse;
import com.project.sns.service.PostService;
import com.project.sns.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  /**
   * POST /api/sns/posts
   */
  @PostMapping
  public ResponseEntity<PostResponse> createPost(
      @ModelAttribute PostRequest request,
      @AuthenticationPrincipal UserVo userVo
  ) {
    request.setUserId(userVo.getId());
    PostResponse response = postService.createPost(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * GET /api/sns/posts/{postId}
   */
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
    return ResponseEntity.ok(postService.getPost(postId));
  }

  /**
   * GET /api/sns/posts
   */
  @GetMapping
  public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable) {
    return ResponseEntity.ok(postService.getAllPosts(pageable));
  }

  /**
   * PUT /api/sns/posts/{postId}
   */
  @PutMapping("/{postId}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable Long postId,
      @ModelAttribute PostRequest request,
      @AuthenticationPrincipal UserVo userVo
  ) {
    request.setUserId(userVo.getId());
    return ResponseEntity.ok(postService.updatePost(postId, request));
  }

  /**
   * DELETE /api/sns/posts/{postId}
   */
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserVo userVo
  ) {
    postService.deletePost(postId, userVo.getId());
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /api/sns/posts/trending
   */
  @GetMapping("/trending")
  public ResponseEntity<Page<PostResponse>> getTrendingPosts(Pageable pageable) {
    return ResponseEntity.ok(postService.getTrendingPosts(pageable));
  }
}
