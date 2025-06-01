package com.project.order.kafka;

import com.project.order.dto.OrderFilledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka 주문 체결 이벤트 발행 서비스
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

  private final KafkaTemplate<String, OrderFilledEvent> kafkaTemplate;

  private static final String TOPIC = "order.filled";

  public void sendOrderFilledEvent(OrderFilledEvent event) {
    kafkaTemplate.send(TOPIC, event);
  }
}