package com.project.order.kafka;

import com.project.order.dto.OrderFilledEvent;
import com.project.order.dto.PortfolioUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka 주문 체결 이벤트 발행 서비스
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

  // OrderFilledEvent 전용 KafkaTemplate
  private final KafkaTemplate<String, OrderFilledEvent> orderFilledKafkaTemplate;

  // PortfolioUpdateEvent 전용 KafkaTemplate
  private final KafkaTemplate<String, PortfolioUpdateEvent> portfolioUpdateKafkaTemplate;

  private static final String ORDER_TOPIC = "order.filled";
  private static final String PORTFOLIO_TOPIC = "portfolio.update";

  /**
   * 체결 이벤트 발행
   *
   * @param event - 주문 체결 이벤트
   */
  public void sendOrderFilledEvent(OrderFilledEvent event) {
    orderFilledKafkaTemplate.send(ORDER_TOPIC, event);
  }

  /**
   * 포트폴리오 자산/현금 반영 이벤트 발행
   *
   * @param event - 포트폴리오 업데이트 이벤트
   */
  public void sendPortfolioUpdateEvent(PortfolioUpdateEvent event) {
    portfolioUpdateKafkaTemplate.send(PORTFOLIO_TOPIC, event);
  }
}