package com.project.price.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashAccountDto {
    private Long accountId;
    private Long userId;
    private BigDecimal balance;
    private String createdAt;
    private String updatedAt;
}
