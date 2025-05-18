package com.project.price.controller;

import com.project.price.dto.CashAccountDto;
import com.project.price.service.CashAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/cash-account")
@RequiredArgsConstructor
public class CashAccountController {
    private final CashAccountService cashAccountService;

    @PostMapping("/create")
    public ResponseEntity<CashAccountDto> create(@RequestParam Long userId) {
        return ResponseEntity.ok(cashAccountService.createAccount(userId));
    }
}
