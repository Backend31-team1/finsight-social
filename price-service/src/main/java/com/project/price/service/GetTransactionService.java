package com.project.price.service;

import com.project.common.dto.TransactionDto;
import com.project.price.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTransactionService {

    private final TransactionRepository transactionRepository;

    // 다건 조회
    public List<TransactionDto> getTransactionsByIds(List<Long> transactionIds) {
        return transactionRepository.findAllById(transactionIds)
                .stream()
                .map(tx -> new TransactionDto(
                        tx.getTransactionId(),
                        tx.getAssetId(),
                        tx.getQuantity(),
                        tx.getTotalPrice(),
                        tx.getTradeAt(),
                        tx.getTransactionCountry()
                ))
                .toList();
    }
}
