package com.project.sns.repository;

import com.project.sns.entity.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatsRepository extends JpaRepository<PostStats, Long> {

  PostStats findByPostPostId(Long postId);

}
