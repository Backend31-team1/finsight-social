package com.project.sns.service;

import com.project.sns.dto.PostRequest;
import com.project.sns.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {

  PostResponse createPost(PostRequest postRequest);
  PostResponse getPost(Long postId);
  Page<PostResponse> getAllPosts(Pageable pageable);
  PostResponse updatePost(Long postId, PostRequest request);
  void deletePost(Long postId, Long userId);
  void recordView(Long postId);
  Page<PostResponse> getTrendingPosts(Pageable pageable);

}
