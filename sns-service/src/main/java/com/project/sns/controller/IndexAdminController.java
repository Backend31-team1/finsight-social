// IndexAdminController.java
package com.project.sns.controller;

import com.project.sns.service.IndexAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/admin")
public class IndexAdminController {

    private final IndexAdminService indexAdminService;

    /**
     * POST /api/sns/admin/create-users-index : Elasticsearch 유저 인덱싱
     */
    @PostMapping("/create-users-index")
    public ResponseEntity<String> createUsersIndex() {
        try {
            indexAdminService.createUsersIndex();
            return ResponseEntity.ok("users 인덱스 생성 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("users 인덱스 생성 실패: " + e.getMessage());
        }
    }

    /**
     * POST /api/sns/admin/create-posts-index : Elasticsearch 게시글 인덱싱
     */
    @PostMapping("/create-posts-index")
    public ResponseEntity<String> createPostsIndex() {
        try {
            indexAdminService.createPostsIndex();
            return ResponseEntity.ok("posts 인덱스 생성 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("posts 인덱스 생성 실패: " + e.getMessage());
        }
    }
}
