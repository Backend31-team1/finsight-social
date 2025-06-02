package com.project.sns.repository;

import com.project.sns.entity.Like;
import com.project.sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUserId(Post post, Long userId);
    long countByPost(Post post);
}
