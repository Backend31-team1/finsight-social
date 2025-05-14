package com.project.sns.dto;

import com.project.sns.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDto {
  private Long notificationId;
  private String title;
  private String message;
  private NotificationType notificationType;
  private Boolean isRead;
  private LocalDateTime createdAt;
}