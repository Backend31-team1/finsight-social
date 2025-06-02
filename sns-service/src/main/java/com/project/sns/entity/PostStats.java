package com.project.sns.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 게시글 통계(PostStats) 엔티티
 * • ERD 상에서 조회용 게시글 가중치 스탯 테이블(PostStats)과 1대1 매핑
 * • 좋아요 수, 댓글 수, 조회 수, 대댓글 수 등을 관리
 * • Post 엔티티와 1:1 관계를 맺고, FK(post_id)를 PostStats가 관리
 */
@Entity
@Table(name = "poststats")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostStats {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "poststats_id")
  private Long postStatsId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false, unique = true)
  private Post post;

  @Column(name = "like_count", nullable = false, columnDefinition = "integer default 0")
  private Integer likeCount;

  @Column(name = "comment_count", nullable = false, columnDefinition = "integer default 0")
  private Integer commentCount;

  /**
   * 조회 수 (실시간 집계할 때 Redis에서 읽어와 DB에 동기화하거나 recordView 메서드 호출 시 증가)
   */
  @Column(name = "view_count", nullable = false, columnDefinition = "integer default 0")
  private Integer viewCount;

  /**
   * 대댓글(하위 댓글) 수
   */
  @Column(name = "under_comment_count", nullable = false, columnDefinition = "integer default 0")
  private Integer underCommentCount;

  //===========================================================
  // 아래부터는 값을 증가시키기 위한 가중치 메서드들이고 호출 시에, 카운트를 1씩 증가하게 구현했습니다.
  //===========================================================

  public void incrementLike() {
    this.likeCount = this.likeCount + 1;
  }

  public void incrementComment() {
    this.commentCount = this.commentCount + 1;
  }

  public void incrementView() {
    this.viewCount = this.viewCount + 1;
  }

  public void incrementUnderComment() {
    this.underCommentCount = this.underCommentCount + 1;
  }
}
