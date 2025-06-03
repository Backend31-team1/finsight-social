package com.project.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 주문 요청 DTO
 *
 * 클라이언트로부터 받는 주문 정보입니다.
 * userId는 JWT 토큰에서 추출하므로 요청에 포함하지 않습니다.
 */
@Data
public class OrderRequest {

  @NotNull(message = "종목 ID는 필수입니다")
  private Long assetId;

  @NotNull(message = "포트폴리오 ID는 필수입니다")
  private Long portfolioId;

  @Min(value = 1, message = "수량은 1 이상이어야 합니다")
  private Integer quantity;

  @DecimalMin(value = "0.01", message = "가격은 0보다 커야 합니다")
  private BigDecimal targetPrice; // 지정가 주문에 사용

  @NotNull(message = "주문 유형은 필수입니다")
  private String orderType; // "MARKET" 또는 "LIMIT"

  @NotNull(message = "매수/매도 구분은 필수입니다")
  private Boolean isBuy; // true: 매수, false: 매도

  private Long ttl; // 유효 시간 (선택적)
}