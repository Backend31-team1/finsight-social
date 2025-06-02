package com.project.price.service;

import com.project.common.dto.UserSummaryDto;
import com.project.price.client.UserClient;
import com.project.price.dto.CashAccountDto;
import com.project.price.entity.CashAccount;
import com.project.price.repository.CashAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashAccountService {

    private final CashAccountRepository cashAccountRepository;
    private final UserClient userClient;

    //계좌 생성
    public CashAccountDto createAccount(Long userId) {
        UserSummaryDto user = userClient.getUserSummary(userId);

        CashAccount account = CashAccount.builder()
                .userId(user.getUserId())
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CashAccount saved = cashAccountRepository.save(account);
        return toDto(saved);
    }

    //계좌 조회
    public List<CashAccountDto> getAccountsByUserId(Long userId) {
        UserSummaryDto user = userClient.getUserSummary(userId);

        List<CashAccount> accounts = cashAccountRepository.findAllByUserId(user.getUserId());
        return accounts.stream().map(this::toDto).toList();
    }

    //Entity -> Dto 변환
    private CashAccountDto toDto(CashAccount account) {
        return CashAccountDto.builder()
                .accountId(account.getAccountId())
                .userId(account.getUserId())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt().toString())
                .updatedAt(account.getUpdatedAt().toString())
                .build();
    }
}
