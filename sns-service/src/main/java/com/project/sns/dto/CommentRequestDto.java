package com.project.sns.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CommentRequestDto {
  private Long parentCommentId;
  private String content;
}
