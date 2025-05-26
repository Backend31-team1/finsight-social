package com.project.order.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 주문 요청 DTO
 */
@Data
public class OrderRequest {
  private Long assetId;
  private Long portfolioId;
  private Integer quantity;
  private BigDecimal targetPrice; // 지정가 주문에 사용
  private Long ttl; // 유효 시간 (선택적)
}