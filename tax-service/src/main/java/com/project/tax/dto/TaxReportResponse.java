package com.project.tax.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public class TaxReportResponse {
    private Long userId;
    private BigDecimal domesticTotalProfit;
    private BigDecimal foreignTotalProfit;
    private BigDecimal domesticTax;
    private BigDecimal foreignTax;
    private BigDecimal totalTax;
    private LocalDateTime reportedAt;
}
