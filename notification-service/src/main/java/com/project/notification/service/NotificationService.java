package com.project.notification.service;

import com.project.notification.dto.OrderFilledEvent;
import com.project.notification.entity.Notification;
import com.project.notification.enums.NotificationType;
import com.project.notification.repository.NotificationRepository;
import com.project.notification.websocket.NotificationSocketSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 알림(Notification) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationSocketSender notificationSocketSender;

  /**
   * Kafka에서 수신한 order.filled 이벤트를 처리합니다.
   * - 알림 DB에 저장하고, 해당 사용자에게 WebSocket으로 실시간 알림을 전송합니다.
   *
   * @param event 주문 체결 이벤트 DTO
   */
  public void handleOrderFilledEvent(OrderFilledEvent event) {
    // 알림 메시지 생성
    String message = String.format("주문 체결: %s %.4f개 @ %.2f원",
        event.getAssetId(),
        event.getQuantity(),
        event.getExecutedPrice());

    // 알림 엔티티 생성 및 저장
    Notification notification = Notification.builder()
        .userId(event.getUserId())
        .title("주문 체결 알림")
        .message(message)
        .notificationType(NotificationType.ORDER)
        .isRead(false)
        .createdAt(LocalDateTime.now())
        .build();

    notificationRepository.save(notification);

    // WebSocket 전송
    notificationSocketSender.send(event.getUserId(), message);
  }
}