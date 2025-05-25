package com.project.order.entity;

/**
 * 주문 유형 ENUM
 */
public enum OrderType {
  MARKET, LIMIT;

  public boolean isBuy() {
    // 예시: MARKET 타입이면 매수라고 가정
    return this == MARKET;
  }

  public boolean isSell() {
    return !isBuy();
  }
}