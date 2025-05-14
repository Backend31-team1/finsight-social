package com.project.sns.entity;

import com.project.sns.enums.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "notification")
@Data
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  private Long userId;

  private String title;

  private String message;

  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  private Boolean isRead = false;

  private LocalDateTime createdAt;
}
