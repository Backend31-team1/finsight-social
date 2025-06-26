package com.project.report.controller;

import com.project.common.dto.ProfitItemDto;
import com.project.report.repository.ProfitItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/report/profit-items")
@RequiredArgsConstructor
public class ProfitItemController {

    private final ProfitItemRepository profitItemRepository;

    /**
     * GET /api/report/profit-items?userId={userId}&start={start}&end={end}
     */
    @GetMapping
    public ResponseEntity<List<ProfitItemDto>> getProfitItemsByUserAndDate(
        @RequestParam Long userId,
        @RequestParam String start,
        @RequestParam String end
    ) {
        LocalDateTime startDt = LocalDateTime.parse(start);
        LocalDateTime endDt   = LocalDateTime.parse(end);

        List<ProfitItemDto> result = profitItemRepository
            .findByUserIdAndCreatedAtBetween(userId, startDt, endDt)
            .stream()
            .map(item -> ProfitItemDto.builder()
                .transactionId(item.getTransactionId())
                .createdAt(item.getCreatedAt())
                .build())
            .toList();

        return ResponseEntity.ok(result);
    }
}
