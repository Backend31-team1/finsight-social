package com.project.price.controller;

import com.project.price.dto.CashAccountDto;
import com.project.price.service.CashAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/cash-account")
@RequiredArgsConstructor
public class CashAccountController {
    private final CashAccountService cashAccountService;

    //계좌 생성
    @PostMapping("/create")
    public ResponseEntity<CashAccountDto> create(@RequestParam Long userId) {
        return ResponseEntity.ok(cashAccountService.createAccount(userId));
    }

    //계좌 조회
    @GetMapping("/list")
    public ResponseEntity<List<CashAccountDto>> getAccountsByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(cashAccountService.getAccountsByUserId(userId));
    }
}
