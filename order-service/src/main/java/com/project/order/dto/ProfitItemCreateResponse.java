package com.project.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ProfitItem 생성 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitItemCreateResponse {
  private Long id;            // ProfitItem ID
  private Long userId;        // 사용자 ID
  private Long assetId;       // 자산 ID
  private Long transactionId; // 거래 ID
  private LocalDateTime createdAt; // 생성 시간
}