package com.project.price.controller;

import com.project.common.dto.TransactionDto;
import com.project.price.service.GetTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price/transaction")
@RequiredArgsConstructor
public class GetTransactionController {

    private final GetTransactionService getTransactionService;

    /**
     * 다건 조회
     * POST /api/price/transaction/batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<TransactionDto>> getTransactionsByIds(
        @RequestBody List<Long> transactionIds
    ) {
        return ResponseEntity.ok(getTransactionService.getTransactionsByIds(transactionIds));
    }
}
