package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserVo user
    ) {
        Map<String, Object> result = postLikeService.toggleLike(postId, user.getId());
        return ResponseEntity.ok(result);
    }
}
