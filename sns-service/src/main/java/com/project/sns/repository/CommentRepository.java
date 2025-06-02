package com.project.sns.repository;

import com.project.sns.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  // 게시글 ID 기준으로 댓글 시간순 정렬
  List<Comment> findByPost_PostIdOrderByCreatedAtAsc(Long postId);
}