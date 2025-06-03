package com.project.sns.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * order-service에서 전송하는 체결 알림 메시지 DTO
 */
@Getter
public class OrderFilledEvent {
  private Long orderId;
  private Long portfolioId;
  private String assetId;
  private BigDecimal quantity;
  private BigDecimal price;
  private Instant executedAt;
}