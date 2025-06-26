// NotificationController.java
package com.project.sns.controller;

import com.project.sns.dto.NotificationResponseDto;
import com.project.sns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * GET /api/sns/notifications : 내 알림 조회
   */
  @GetMapping
  public ResponseEntity<List<NotificationResponseDto>> getMyNotifications() {
    return ResponseEntity.ok(notificationService.getUserNotifications(1L)); // 임시 userId
  }

  /**
   * PATCH /api/sns/notifications/{id}/read : 알림 읽음 처리
   */
  @PatchMapping("/{id}/read")
  public ResponseEntity<String> markAsRead(@PathVariable("id") Long id) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok("알림 읽음 처리 완료");
  }
}
