package com.project.order.service;

import com.project.order.dto.OrderFilledEvent;
import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.kafka.KafkaProducerService;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 주문 체결 후 자산 반영 및 Kafka 이벤트 발행
 */
@Service
@RequiredArgsConstructor
public class ExecutionService {

  private final OrderRepository orderRepository;
  private final KafkaProducerService kafkaProducerService;

  @Transactional
  public void processExecution(Order order) {
    // 매수/매도 처리 생략

    order.setStatus(OrderStatus.FILLED);
    orderRepository.save(order);

    // Kafka 이벤트 발행
    kafkaProducerService.sendOrderFilledEvent(
        OrderFilledEvent.builder()
            .orderId(order.getId())
            .portfolioId(order.getPortfolioId())
            .assetId(order.getAssetId().toString())
            .quantity(BigDecimal.valueOf(order.getQuantity()))
            .price(order.getTargetPrice())
            .executedAt(order.getExecutedAt().toInstant())
            .build()
    );
  }
}