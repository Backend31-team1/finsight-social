package com.project.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 주문 엔티티
 *
 * 가상 주식 거래의 주문 정보를 저장합니다.
 * userId를 통해 직접적인 사용자 권한 검증을 수행하며,
 * portfolioId는 단순 참조용으로만 사용됩니다.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 주문한 사용자 ID - 권한 검증의 핵심 필드
   */
  @Column(name = "user_id", nullable = false)
  private Long userId;

  /**
   * 포트폴리오 ID - 단순 참조용 (의존성 없음)
   */
  @Column(name = "portfolio_id", nullable = false)
  private Long portfolioId;

  @Column(name = "asset_id", nullable = false)
  private Long assetId;

  @Enumerated(EnumType.STRING)
  @Column(name = "order_type", nullable = false)
  private OrderType orderType; // MARKET, LIMIT

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "target_price", precision = 15, scale = 2)
  private BigDecimal targetPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private OrderStatus status; // PENDING, FILLED, CANCELLED

  @Column(name = "ttl")
  private Long ttl;

  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt;

  @Column(name = "executed_at")
  private Timestamp executedAt;

  /**
   * 매수/매도 구분
   * true: 매수, false: 매도
   */
  @Column(name = "is_buy", nullable = false)
  private Boolean isBuy;
}