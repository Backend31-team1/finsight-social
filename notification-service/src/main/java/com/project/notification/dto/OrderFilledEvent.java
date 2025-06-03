package com.project.notification.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Kafka order.filled 이벤트 수신 시 역직렬화를 위한 DTO 클래스입니다.
 * - 주문 체결 정보를 포함하고 있습니다.
 */
@Data
public class OrderFilledEvent {

  // 주문한 사용자 ID
  private Long userId;

  // 주문 ID
  private Long orderId;

  // 자산 ID (예: AAPL, BTC 등)
  private String assetId;

  // 체결된 수량
  private BigDecimal quantity;

  // 체결 가격
  private BigDecimal executedPrice;

  // 체결 시각 (서버 기준)
  private Timestamp executedAt;
}