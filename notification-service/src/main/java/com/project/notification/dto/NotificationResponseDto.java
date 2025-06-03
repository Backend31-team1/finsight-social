package com.project.notification.dto;

import com.project.notification.entity.Notification;
import com.project.notification.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 알림 조회 응답용 DTO 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

  private Long id;
  private String title;
  private String message;
  private NotificationType notificationType;
  private Boolean isRead;
  private LocalDateTime createdAt;

  /**
   * Notification 엔티티를 DTO로 변환하는 정적 팩토리 메서드
   */
  public static NotificationResponseDto from(Notification notification) {
    return NotificationResponseDto.builder()
        .id(notification.getId())
        .title(notification.getTitle())
        .message(notification.getMessage())
        .notificationType(notification.getNotificationType())
        .isRead(notification.getIsRead())
        .createdAt(notification.getCreatedAt())
        .build();
  }
}