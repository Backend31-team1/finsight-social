package com.project.sns.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 스펙 때문에 기본 생성자는 protected로 둠
@AllArgsConstructor
@Builder
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long postId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "title", nullable = false)
  private String title;

  @Lob   // TEXT 타입으로 매핑
  @Column(name = "content", nullable = false)
  private String content;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "views", nullable = false, columnDefinition = "integer default 0")
  private Integer views;

  @Column(name = "post_image_url")
  private String postImageUrl;

  @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private PostStats postStats;

  // 댓글 매핑
  @OneToMany(
      mappedBy = "post",
      cascade = CascadeType.REMOVE,   // Post 삭제 시 댓글도 함께 삭제
      orphanRemoval = true,           // 연관관계 제거 시 해당 자식 엔티티도 삭제
      fetch = FetchType.LAZY
  )
  private List<Comment> comments = new ArrayList<>();

  // 게시글 좋아요 매핑
  @OneToMany(
      mappedBy = "post",
      cascade = CascadeType.REMOVE,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<Like> likes = new ArrayList<>();

  public void update(String newTitle, String newContent, String newImageUrl) {
    this.title = newTitle;
    this.content = newContent;
    this.postImageUrl = newImageUrl;
  }
}

