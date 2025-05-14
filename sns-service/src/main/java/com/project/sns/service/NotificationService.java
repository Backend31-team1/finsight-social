package com.project.sns.service;

import com.project.sns.dto.NotificationResponseDto;
import com.project.sns.entity.Notification;
import com.project.sns.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 알림(Notification) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  /**
   * 사용자 ID로 알림 목록을 조회합니다.
   *
   * @param userId 사용자 ID
   * @return 알림 응답 DTO 리스트
   */
  public List<NotificationResponseDto> getUserNotifications(Long userId) {
    List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

    return notifications.stream().map(n -> {
      NotificationResponseDto dto = new NotificationResponseDto();
      dto.setNotificationId(n.getNotificationId());
      dto.setTitle(n.getTitle());
      dto.setMessage(n.getMessage());
      dto.setNotificationType(n.getNotificationType());
      dto.setIsRead(n.getIsRead());
      dto.setCreatedAt(n.getCreatedAt());
      return dto;
    }).collect(Collectors.toList());
  }

  /**
   * 특정 알림을 읽음 처리합니다.
   *
   * @param notificationId 읽을 알림 ID
   * @throws IllegalArgumentException 알림이 존재하지 않을 경우
   */
  public void markAsRead(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));

    notification.setIsRead(true);
    notificationRepository.save(notification);
  }
}