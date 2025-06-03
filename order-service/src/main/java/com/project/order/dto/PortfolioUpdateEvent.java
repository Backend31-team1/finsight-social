package com.project.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 현금 또는 자산 업데이트를 위한 Kafka 메시지 DTO
 */
@Getter
@Builder
public class PortfolioUpdateEvent {

  private Long portfolioId;
  private String assetId;
  private BigDecimal quantity;
  private BigDecimal amount;  // 금액 (현금용)
  private ActionType action;  // 예: INCREASE_CASH, DECREASE_ASSET 등

  public enum ActionType {
    INCREASE_CASH,
    DECREASE_CASH,
    INCREASE_ASSET,
    DECREASE_ASSET
  }
}