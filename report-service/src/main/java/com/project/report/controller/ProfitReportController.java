package com.project.report.controller;

import com.project.report.dto.ProfitReport;
import com.project.report.service.ProfitReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/report")
@RequiredArgsConstructor
public class ProfitReportController {

    private final ProfitReportService profitReportService;

    // 수익 레포트 조회(주간, 월간)
    @GetMapping
    public ResponseEntity<ProfitReport> getProfitReport(
            @RequestParam Long userId,
            @RequestParam String type // "weekly" or "monthly"
    ) {
        return ResponseEntity.ok(profitReportService.getProfitReport(userId, type));
    }
}
