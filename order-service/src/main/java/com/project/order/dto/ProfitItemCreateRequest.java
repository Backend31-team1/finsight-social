package com.project.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ProfitItem 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitItemCreateRequest {
  private Long userId;        // 사용자 ID
  private Long assetId;       // 자산 ID
  private Long transactionId; // 거래 ID
}