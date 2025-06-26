package com.project.tax.controller;

import com.project.tax.entity.TaxReport;
import com.project.tax.service.TaxReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax-report")
@RequiredArgsConstructor
public class TaxReportController {

    private final TaxReportService taxReportService;

    /**
     * GET /api/tax-report/generate?userId={userId}
     */
    @GetMapping("/generate")
    public ResponseEntity<TaxReport> generate(@RequestParam Long userId) {
        return ResponseEntity.ok(
            taxReportService.generateAndSaveAnnualTaxReport(userId)
        );
    }
}
