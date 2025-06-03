package com.project.order.service;

import com.project.order.client.ReportServiceClient;
import com.project.order.dto.ProfitItemCreateRequest;
import com.project.order.entity.Order;
import com.project.order.entity.Transaction;
import com.project.order.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * 거래 내역 생성 및 관리 서비스 (의존성 최소화 버전)
 *
 * 주문 체결 후 거래 내역을 생성합니다.
 * ReportServiceClient는 선택적 의존성으로 처리하여 없어도 동작합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  // ReportServiceClient를 선택적 의존성으로 처리
  @Autowired(required = false)
  private ReportServiceClient reportServiceClient;

  /**
   * 주문 정보를 기반으로 거래 내역을 생성하여 저장합니다.
   *
   * 매도 거래인 경우 Report Service가 활성화되어 있으면 ProfitItem 생성을 요청합니다.
   *
   * @param order 체결된 주문 정보
   */
  @Transactional
  public void saveTransaction(Order order) {
    // Transaction 저장
    Transaction transaction = Transaction.builder()
        .orderId(order.getId())
        .portfolioId(order.getPortfolioId())
        .assetId(order.getAssetId().toString())
        .quantity(order.getQuantity())
        .price(order.getTargetPrice())
        .orderType(order.getOrderType())
        .isBuy(order.getIsBuy())
        .executedAt(order.getExecutedAt() != null ?
            order.getExecutedAt() :
            new Timestamp(System.currentTimeMillis()))
        .build();

    Transaction savedTransaction = transactionRepository.save(transaction);

    // 매도 거래인 경우 ProfitItem 생성 시도 (ReportService가 있을 때만)
    if (!order.getIsBuy()) {
      createProfitItemIfAvailable(savedTransaction, order);
    }
  }

  /**
   * Report Service가 활성화되어 있을 때만 ProfitItem을 생성합니다.
   *
   * Report Service가 없어도 거래 처리에는 영향을 주지 않습니다.
   *
   * @param transaction 저장된 거래 내역
   * @param order 원본 주문
   */
  private void createProfitItemIfAvailable(Transaction transaction, Order order) {
    if (reportServiceClient == null) {
      log.debug("ReportServiceClient가 비활성화되어 있어 ProfitItem 생성을 건너뜁니다.");
      return;
    }

    try {
      ProfitItemCreateRequest request = ProfitItemCreateRequest.builder()
          .userId(order.getUserId())
          .assetId(order.getAssetId())
          .transactionId(transaction.getId())
          .build();

      reportServiceClient.createProfitItem(request);

    } catch (Exception e) {
      // ProfitItem 생성 실패가 거래를 방해하지 않도록 로그만 기록
      log.warn("ProfitItem 생성 실패 - Transaction: {}, 에러: {}",
          transaction.getId(), e.getMessage());
    }
  }
}