package com.project.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 포트폴리오 업데이트를 위한 Kafka 이벤트 DTO (강화 버전)
 *
 * Order Service에서 Portfolio Service로 전송하는 이벤트입니다.
 * 잔액 검증과 실제 업데이트를 분리하여 처리합니다.
 */
@Getter
@Builder
public class PortfolioUpdateEvent {

  private Long orderId;        // 주문 ID (추적용)
  private Long userId;         // 사용자 ID (권한 검증용)
  private Long portfolioId;    // 포트폴리오 ID
  private String assetId;      // 자산 ID
  private BigDecimal quantity; // 수량
  private BigDecimal amount;   // 금액 (현금용)
  private ActionType action;   // 액션 유형
  private boolean isValidation; // 검증 요청인지 실제 업데이트인지 구분

  public enum ActionType {
    // 현금 관련
    INCREASE_CASH,     // 현금 증가 (매도)
    DECREASE_CASH,     // 현금 감소 (매수)

    // 자산 관련
    INCREASE_ASSET,    // 자산 증가 (매수)
    DECREASE_ASSET,    // 자산 감소 (매도)

    // 검증 관련
    VALIDATE_CASH,     // 현금 잔액 검증
    VALIDATE_ASSET     // 자산 보유량 검증
  }
}