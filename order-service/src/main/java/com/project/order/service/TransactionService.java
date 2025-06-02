package com.project.order.service;

import com.project.order.entity.Order;
import com.project.order.entity.Transaction;
import com.project.order.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 주문 체결 후 거래 내역을 생성하고 저장하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  /**
   * 주문 정보를 기반으로 거래 내역을 생성하여 저장합니다.
   *
   * @param order - 체결된 주문 정보
   */
  public void saveTransaction(Order order) {
    Transaction transaction = Transaction.builder()
        .orderId(order.getId())
        .portfolioId(order.getPortfolioId())
        .assetId(order.getAssetId().toString())
        .quantity(order.getQuantity())
        .price(order.getTargetPrice())
        .orderType(order.getOrderType())
        .executedAt(order.getExecutedAt() != null ? order.getExecutedAt() : new Timestamp(System.currentTimeMillis()))
        .build();

    transactionRepository.save(transaction); // 거래 내역 저장
  }
}