package com.project.sns.controller;

import com.project.sns.service.PostMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/migrate")
public class PostMigrationController {

    private final PostMigrationService postMigrationService;

    // 게시글 마이그레이션
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
