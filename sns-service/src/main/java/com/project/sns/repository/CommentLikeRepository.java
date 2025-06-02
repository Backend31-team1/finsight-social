package com.project.sns.repository;

import com.project.sns.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    // 댓글 ID + 사용자 ID로 좋아요 여부 확인
    Optional<CommentLike> findByComment_CommentIdAndUserId(Long commentId, Long userId);

    // 댓글 ID 기준 좋아요 수 조회
    Long countByComment_CommentId(Long commentId);
}
