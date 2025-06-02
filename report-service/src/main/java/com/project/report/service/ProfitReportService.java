package com.project.report.service;

import com.project.common.dto.TransactionDto;
import com.project.report.client.TransactionClient;
import com.project.report.dto.ProfitReport;
import com.project.report.entity.ProfitItem;
import com.project.report.repository.ProfitItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfitReportService {

    private final ProfitItemRepository profitItemRepository;
    private final TransactionClient transactionClient;

    public ProfitReport getProfitReport(Long userId, String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = switch (type.toLowerCase()) {
            case "weekly" -> now.minusDays(7);
            case "monthly" -> now.minusMonths(1);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };

        // profitItem에서 유저별 기간 필터링
        List<ProfitItem> items = profitItemRepository
                .findByUserIdAndCreatedAtBetween(userId, start, now);

        if (items.isEmpty()) {
            return ProfitReport.builder()
                    .userId(userId)
                    .type(type)
                    .startDate(start.toLocalDate())
                    .endDate(now.toLocalDate())
                    .totalProfit(BigDecimal.ZERO)
                    .transactions(Collections.emptyList())
                    .build();
        }

        // transactionId 목록 추출
        List<Long> txIds = items.stream()
                .map(ProfitItem::getTransactionId)
                .toList();

        // 외부 거래 서비스에 요청
        List<TransactionDto> txs = transactionClient.getTransactionsByIds(txIds);

        // 총 수익 계산
        BigDecimal totalProfit = txs.stream()
                .map(TransactionDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 응답 반환
        return ProfitReport.builder()
                .userId(userId)
                .type(type)
                .startDate(start.toLocalDate())
                .endDate(now.toLocalDate())
                .totalProfit(totalProfit)
                .transactions(txs)
                .build();
    }
}
