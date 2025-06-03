package com.project.sns.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
  private Long postId;
  private Long userId;
  private String title;
  private String content;
  private String postImageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer views;
  private Integer likeCount;
  private Integer commentCount;
  private Integer underCommentCount;
}

