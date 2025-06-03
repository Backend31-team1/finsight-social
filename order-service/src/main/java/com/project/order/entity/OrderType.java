package com.project.order.entity;

/**
 * ERD에 맞는 주문 유형 ENUM
 * - MARKET: 시장가
 * - LIMIT: 지정가
 * 매수/매도 구분은 Order 엔티티의 isBuy 필드로 처리
 */
public enum OrderType {
  MARKET, LIMIT;

  /**
   * 시장가 주문 여부를 판단합니다.
   */
  public boolean isMarket() {
    return this == MARKET;
  }

  /**
   * 지정가 주문 여부를 판단합니다.
   */
  public boolean isLimit() {
    return this == LIMIT;
  }
}