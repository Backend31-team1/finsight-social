package com.project.sns.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.sns.application.PostApplication;
import com.project.sns.entity.Like;
import com.project.sns.entity.Post;
import com.project.sns.repository.PostLikeRepository;
import com.project.sns.repository.PostRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostApplication postApplication;

    // 게시글 좋아요 처리(토글 형식)
    @Transactional
    public Map<String, Object> toggleLike(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Optional<Like> existingLike = postLikeRepository.findByPostAndUserId(post, userId);

        boolean isLiked;
        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            isLiked = false;
            postApplication.recordUnlike(postId);
        } else {
            Like like = Like.builder()
                    .post(post)
                    .userId(userId)
                    .build();
            postLikeRepository.save(like);
            isLiked = true;
            postApplication.recordLike(postId);
        }

        long likeCount = postLikeRepository.countByPost(post);

        return Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        );
    }
}
