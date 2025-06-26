// PostLikeController.java
package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * POST /api/sns/posts/{postId}/like
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserVo user
    ) {
        return ResponseEntity.ok(postLikeService.toggleLike(postId, user.getId()));
    }
}
