package com.project.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 주문 체결 Kafka 메시지 DTO
 */
@Getter
@Builder
public class OrderFilledEvent {

  private Long orderId;
  private Long portfolioId;
  private String assetId;
  private BigDecimal quantity;
  private BigDecimal price;
  private Instant executedAt;
}