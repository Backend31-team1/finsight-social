package com.project.notification.controller;

import com.project.notification.dto.NotificationResponseDto;
import com.project.notification.entity.Notification;
import com.project.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 알림 조회 및 읽음 처리용 REST API 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationRepository notificationRepository;

  /**
   * 특정 사용자의 알림 목록을 조회합니다.
   *
   * @param userId 사용자 ID
   * @return 알림 목록
   */
  @GetMapping("/{userId}")
  public List<NotificationResponseDto> getUserNotifications(@PathVariable Long userId) {
    List<Notification> notifications = notificationRepository
        .findByUserIdOrderByCreatedAtDesc(userId);

    return notifications.stream()
        .map(NotificationResponseDto::from)
        .collect(Collectors.toList());
  }

  /**
   * 특정 알림을 읽음 처리합니다.
   *
   * @param notificationId 알림 ID
   */
  @PatchMapping("/{notificationId}/read")
  public void markAsRead(@PathVariable Long notificationId) {
    notificationRepository.findById(notificationId).ifPresent(notification -> {
      notification.setIsRead(true);
      notificationRepository.save(notification);
    });
  }
}