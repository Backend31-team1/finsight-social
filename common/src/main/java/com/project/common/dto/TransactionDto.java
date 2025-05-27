package com.project.common.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long transactionId;
    private Long assetId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime tradeAt;
}
