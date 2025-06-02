package com.project.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

  private Long assetId;

  private Long portfolioId;

  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  private Integer quantity;

  private BigDecimal targetPrice;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private Long ttl;

  private Timestamp createdAt;

  private Timestamp executedAt;
}