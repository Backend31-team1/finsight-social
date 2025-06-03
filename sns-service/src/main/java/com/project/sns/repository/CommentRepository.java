package com.project.sns.repository;

import com.project.sns.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 댓글 데이터 접근을 위한 JPA Repository
 *
 * 댓글과 대댓글의 계층형 구조를 고려한 조회 메서드들을 제공합니다.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * 특정 게시글의 모든 댓글을 생성시간 순으로 조회합니다.
   *
   * @param postId 게시글 ID
   * @return 댓글 리스트 (시간순 정렬)
   */
  List<Comment> findByPost_PostIdOrderByCreatedAtAsc(Long postId);

  /**
   * 특정 댓글의 모든 대댓글을 조회합니다.
   *
   * 댓글 삭제 시 해당 댓글의 모든 대댓글을 함께 삭제하기 위해 사용됩니다.
   *
   * @param parentCommentId 부모 댓글 ID
   * @return 대댓글 리스트
   */
  List<Comment> findByParentCommentId(Long parentCommentId);
}