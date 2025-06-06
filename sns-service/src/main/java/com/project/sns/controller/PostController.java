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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  /**
   * 게시글 생성 POST /posts
   */
  @PostMapping
  public ResponseEntity<PostResponse> createPost(
      @ModelAttribute PostRequest request,            // @RequestBody가 아닌 @ModelAttribute -> JSON이 아닌 multipart/form-data
      @AuthenticationPrincipal UserVo userVo
  ) {
    // JWT 인증을 통해 SecurityContext 에 저장된 UserVo 로 userId 얻기
    request.setUserId(userVo.getId());

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
      @ModelAttribute PostRequest request,
      @AuthenticationPrincipal UserVo userVo
  ) {
    request.setUserId(userVo.getId());
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
