package com.project.auth.controller;

import com.project.auth.service.UserMigrationService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserMigrationController {

    private final UserMigrationService userMigrationService;

    @PostMapping("/migrate/users")
    public String migrateUsers() {
        try {
            userMigrationService.migrateUsersToElasticsearch();
            return "Migration success";
        } catch (IOException e) {
            return "Migration failed";
        }
    }
}
