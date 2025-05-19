package com.project.auth.controller;

import com.project.auth.service.GetUserIdService;
import com.project.common.dto.UserIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class GetUserIdController {

    private final GetUserIdService getUserIdService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserIdDto> getUserSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(getUserIdService.getUserId(userId));
    }
}
