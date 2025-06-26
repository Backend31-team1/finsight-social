package com.project.price.controller;

import com.project.price.dto.CashAccountDto;
import com.project.price.service.CashAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price/cash-account")
@RequiredArgsConstructor
public class CashAccountController {

    private final CashAccountService cashAccountService;

    /**
     * 계좌 생성
     * POST /api/price/cash-account/create
     */
    @PostMapping("/create")
    public ResponseEntity<CashAccountDto> create(@RequestParam Long userId) {
        return ResponseEntity.ok(cashAccountService.createAccount(userId));
    }

    /**
     * 계좌 조회
     * GET /api/price/cash-account/list?userId={userId}
     */
    @GetMapping("/list")
    public ResponseEntity<List<CashAccountDto>> getAccountsByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(cashAccountService.getAccountsByUserId(userId));
    }
}
