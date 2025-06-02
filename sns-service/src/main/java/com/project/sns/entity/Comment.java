package com.project.sns.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  private Long userId;

  private Long parentCommentId; // 대댓글이면 상위 댓글 ID, 아니면 null

  private String content;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  // ✅ 댓글 좋아요 연관관계 추가 (Cascade 삭제를 위해 필요)
  @OneToMany(
      mappedBy = "comment",
      cascade = CascadeType.REMOVE,
      orphanRemoval = true
  )
  private List<CommentLike> commentLikes = new ArrayList<>();
}