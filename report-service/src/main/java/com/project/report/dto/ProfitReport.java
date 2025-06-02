package com.project.report.dto;

import com.project.common.dto.TransactionDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public class ProfitReport {
    private Long userId;
    private String type; // "weekly" or "monthly"
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalProfit;
    private List<TransactionDto> transactions;
}

