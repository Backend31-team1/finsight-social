package com.project.auth.controller;

import com.project.auth.service.GetUserSummaryService;
import com.project.common.dto.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
public class GetUserSummaryController {

    private final GetUserSummaryService getUserSummaryService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserSummaryDto> getUserSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(getUserSummaryService.getUserSummary(userId));
    }
}
