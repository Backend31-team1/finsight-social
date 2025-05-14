package com.project.sns.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  private Long postId;

  private Long userId;

  private Long parentCommentId; // 대댓글이면 상위 댓글 ID, 아니면 null

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}