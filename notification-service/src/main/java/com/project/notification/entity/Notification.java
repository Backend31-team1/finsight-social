package com.project.notification.entity;

import com.project.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 사용자 알림 정보를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 알림 수신자 사용자 ID
  private Long userId;

  // 알림 제목
  private String title;

  // 알림 내용
  private String message;

  // 알림 유형 (댓글, 주문 등)
  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  // 읽음 여부
  private Boolean isRead;

  // 알림 생성 시각
  private LocalDateTime createdAt;
}