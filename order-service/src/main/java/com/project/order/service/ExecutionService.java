package com.project.order.service;

import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 주문 체결 후 거래를 처리하는 서비스입니다.
 * 체결된 주문에 따라 포트폴리오의 현금 및 자산을 업데이트합니다.
 */
@Service
@RequiredArgsConstructor
public class ExecutionService {

  private final OrderRepository orderRepository;

  /**
   * 체결된 주문을 처리합니다.
   * 주문 유형에 따라 포트폴리오 현금 및 자산을 업데이트합니다.
   *
   * @param order - 체결된 주문 객체
   */
  @Transactional
  public void processExecution(Order order) {
    // 매수 주문 처리: 현금 차감 및 자산 추가
    if (order.getOrderType().isBuy()) {
      BigDecimal totalAmount = order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
      deductCash(order.getPortfolioId(), totalAmount);
      addAssetToPortfolio(order.getPortfolioId(), order.getAssetId().toString()
          , BigDecimal.valueOf(order.getQuantity()));
    }
    // 매도 주문 처리: 현금 추가 및 자산 제거
    else {
      BigDecimal totalAmount = order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
      addCash(order.getPortfolioId(), totalAmount);
      removeAssetFromPortfolio(order.getPortfolioId(), order.getAssetId().toString()
          , BigDecimal.valueOf(order.getQuantity()));
    }

    // 주문 상태를 FILLED로 업데이트
    order.setStatus(OrderStatus.FILLED);
    orderRepository.save(order);
  }

  /**
   * 포트폴리오에서 지정된 금액만큼 현금을 차감합니다.
   * (추후 외부 서비스 연동 예정)
   *
   * @param portfolioId - 대상 포트폴리오 ID
   * @param amount - 차감할 금액
   */
  private void deductCash(Long portfolioId, BigDecimal amount) {
    // TODO: Kafka 또는 REST API 연동 필요
    System.out.printf("💸 포트폴리오 %d에서 현금 %.2f 차감%n", portfolioId, amount);
  }

  /**
   * 포트폴리오에 지정된 금액만큼 현금을 추가합니다.
   *
   * @param portfolioId - 대상 포트폴리오 ID
   * @param amount - 추가할 금액
   */
  private void addCash(Long portfolioId, BigDecimal amount) {
    System.out.printf("💰 포트폴리오 %d에 현금 %.2f 추가%n", portfolioId, amount);
  }

  /**
   * 포트폴리오에 자산을 추가합니다.
   *
   * @param portfolioId - 대상 포트폴리오 ID
   * @param assetId - 자산 ID
   * @param quantity - 추가할 수량
   */
  private void addAssetToPortfolio(Long portfolioId, String assetId, BigDecimal quantity) {
    System.out.printf("📈 포트폴리오 %d에 자산 %s %.2f개 추가%n", portfolioId, assetId, quantity);
  }

  /**
   * 포트폴리오에서 자산을 제거합니다.
   *
   * @param portfolioId - 대상 포트폴리오 ID
   * @param assetId - 자산 ID
   * @param quantity - 제거할 수량
   */
  private void removeAssetFromPortfolio(Long portfolioId, String assetId, BigDecimal quantity) {
    System.out.printf("📉 포트폴리오 %d에서 자산 %s %.2f개 제거%n", portfolioId, assetId, quantity);
  }
}