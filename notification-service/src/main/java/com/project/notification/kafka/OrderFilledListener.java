package com.project.notification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.notification.dto.OrderFilledEvent;
import com.project.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka 토픽 수신 처리 클래스입니다.
 * - order.filled 토픽을 구독하고, 주문 체결 알림 로직을 수행합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFilledListener {

  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  /**
   * KafkaListener로 order.filled 토픽 수신
   * - 메시지를 JSON 문자열로 수신한 후 OrderFilledEvent로 변환합니다.
   * - 변환된 이벤트를 NotificationService에 위임합니다.
   *
   * @param message Kafka에서 수신된 JSON 문자열 메시지
   */
  @KafkaListener(topics = "order.filled", groupId = "notification-group")
  public void listen(String message) {
    try {
      OrderFilledEvent event = objectMapper.readValue(message, OrderFilledEvent.class);
      log.info("Kafka 수신 - 주문 체결 이벤트: {}", event);

      // 알림 서비스에 처리 위임
      notificationService.handleOrderFilledEvent(event);

    } catch (Exception e) {
      log.error("❌ Kafka 메시지 처리 실패 - message: {}", message, e);
    }
  }
}