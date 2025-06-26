package com.project.report.controller;

import com.project.report.dto.ProfitReport;
import com.project.report.service.ProfitReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ProfitReportController {

    private final ProfitReportService profitReportService;

    /**
     * GET /api/report?userId={userId}&type={weekly|monthly}
     */
    @GetMapping
    public ResponseEntity<ProfitReport> getProfitReport(
        @RequestParam Long userId,
        @RequestParam String type
    ) {
        return ResponseEntity.ok(
            profitReportService.getProfitReport(userId, type)
        );
    }
}
