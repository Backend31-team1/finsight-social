package com.project.sns.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentResponseDto {
  private Long commentId;
  private Long parentCommentId;
  private String content;
  private LocalDateTime createdAt;
}