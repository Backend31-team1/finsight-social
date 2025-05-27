package com.project.tax.service;

import com.project.common.dto.ProfitItemDto;
import com.project.common.dto.TransactionDto;
import com.project.tax.client.ProfitItemClient;
import com.project.tax.client.TransactionClient;
import com.project.tax.entity.TaxReport;
import com.project.tax.repository.TaxReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxReportService {

    private final ProfitItemClient profitItemClient;
    private final TransactionClient transactionClient;
    private final TaxReportRepository taxReportRepository;

    // 사용자의 올해 거래 정보를 기반으로 세금 리포트를 계산하고 저장
    @Transactional
    public TaxReport generateAndSaveAnnualTaxReport(Long userId) {
        // 올해 1월 1일부터 현재까지 기간 설정
        LocalDateTime start = LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        // ProfitItem(매도 정보) 조회
        List<ProfitItemDto> items = profitItemClient.getProfitItemsByUserAndDate(
                userId,
                start.toString(),
                end.toString()
        );

        // ProfitItem에서 거래 ID만 추출
        List<Long> txIds = items.stream().map(ProfitItemDto::getTransactionId).toList();

        // 거래 서비스에서 거래 상세정보(DTO) 리스트 조회
        List<TransactionDto> transactions = transactionClient.getTransactionsByIds(txIds);

        // 거래를 국내/해외로 구분하고 수익 합산
        BigDecimal domesticProfit = BigDecimal.ZERO;
        BigDecimal foreignProfit = BigDecimal.ZERO;

        for (TransactionDto tx : transactions) {
            if ("DOMESTIC".equalsIgnoreCase(String.valueOf(tx.getCountry()))) {
                domesticProfit = domesticProfit.add(tx.getTotalPrice());
            } else {
                foreignProfit = foreignProfit.add(tx.getTotalPrice());
            }
        }

        // 국내: 0.15% 세금 계산
        BigDecimal domesticTax = domesticProfit.multiply(new BigDecimal("0.0015"));

        // 해외: 250만 원 초과분에 대해 22% 세금 계산
        BigDecimal exemption = new BigDecimal("2500000");
        BigDecimal foreignTax = foreignProfit.compareTo(exemption) > 0
                ? foreignProfit.subtract(exemption).multiply(new BigDecimal("0.22"))
                : BigDecimal.ZERO;

        // 총 세금 계산
        BigDecimal totalTax = domesticTax.add(foreignTax);

        // TaxReport 엔티티로 생성하고 저장
        TaxReport report = TaxReport.builder()
                .userId(userId)
                .domesticTotalProfit(domesticProfit)
                .foreignTotalProfit(foreignProfit)
                .domesticTax(domesticTax)
                .foreignTax(foreignTax)
                .totalTax(totalTax)
                .reportedAt(LocalDateTime.now())
                .build();

        return taxReportRepository.save(report);
    }
}
