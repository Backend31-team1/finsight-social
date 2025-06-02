package com.project.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 주문 체결 시 생성되는 거래 내역을 나타내는 엔티티입니다.
 */
@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 거래 ID (자동 생성)

  private Long orderId;         // 주문 ID
  private Long portfolioId;     // 포트폴리오 ID
  private String assetId;       // 자산 ID (종목 코드)

  private Integer quantity;     // 체결 수량
  private BigDecimal price;     // 체결 가격

  @Enumerated(EnumType.STRING)
  private OrderType orderType;  // 주문 유형 (MARKET, LIMIT 등)

  private Timestamp executedAt; // 체결 시간
}