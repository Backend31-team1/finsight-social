// PostMigrationController.java
package com.project.sns.controller;

import com.project.sns.service.PostMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/migrate")
public class PostMigrationController {

    private final PostMigrationService postMigrationService;

    /**
     * POST /api/sns/migrate/posts
     */
    @PostMapping("/posts")
    public String migratePosts() {
        try {
            postMigrationService.migratePostsToElasticsearch();
            return "Post migration success";
        } catch (IOException e) {
            return "Post migration failed";
        }
    }
}
