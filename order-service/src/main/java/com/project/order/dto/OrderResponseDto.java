package com.project.order.dto;

import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 주문 정보 응답 DTO
 */
@Getter
@Builder
public class OrderResponseDto {

  private Long id;
  private Long portfolioId;
  private String assetId;
  private OrderType orderType;
  private BigDecimal quantity;
  private BigDecimal targetPrice;
  private OrderStatus status;
  private Timestamp createdAt;
  private Timestamp executedAt;
}