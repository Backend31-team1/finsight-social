package com.project.sns.controller;

import com.project.sns.dto.NotificationResponseDto;
import com.project.sns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationResponseDto>> getMyNotifications() {
    Long mockUserId = 1L; // 임시 사용자 ID (로그인 연동 전)
    List<NotificationResponseDto> notifications = notificationService.getUserNotifications(mockUserId);
    return ResponseEntity.ok(notifications);
  }

  // 읽음 처리 API
  @PatchMapping("/{id}/read")
  public ResponseEntity<String> markAsRead(@PathVariable("id") Long id) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok("알림 읽음 처리 완료");
  }
}