package com.project.auth.controller;

import com.project.auth.service.UserMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/migrate")
public class UserMigrationController {

    private final UserMigrationService userMigrationService;

    //유저 마이그레이션
    @PostMapping("/users")
    public String migrateUsers() {
        try {
            userMigrationService.migrateUsersToElasticsearch();
            return "Migration success";
        } catch (IOException e) {
            return "Migration failed";
        }
    }
}
