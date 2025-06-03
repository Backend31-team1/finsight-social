package com.project.order.service;

import com.project.order.dto.OrderFilledEvent;
import com.project.order.dto.PortfolioUpdateEvent;
import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.kafka.KafkaProducerService;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * 주문 체결 처리 및 후속 작업을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class ExecutionService {

  private final OrderRepository orderRepository;
  private final KafkaProducerService kafkaProducerService;
  private final TransactionService transactionService;

  /**
   * 주문 체결 처리를 수행합니다.
   * 체결 완료 시 거래 내역을 저장하고 Kafka 이벤트를 발행합니다.
   * 매도 거래인 경우 ProfitItem도 함께 생성됩니다.
   *
   * @param order 체결할 주문 객체
   */
  @Transactional
  public void processExecution(Order order) {
    // 주문 상태 업데이트
    order.setStatus(OrderStatus.FILLED);
    order.setExecutedAt(Timestamp.from(Instant.now()));
    orderRepository.save(order);

    // 거래 내역 저장 (매도인 경우 ProfitItem도 함께 생성)
    transactionService.saveTransaction(order);

    // 포트폴리오 업데이트 이벤트 발행
    publishPortfolioUpdateEvents(order);

    // 주문 체결 알림 이벤트 발행
    publishOrderFilledEvent(order);
  }

  /**
   * 포트폴리오 자산/현금 업데이트 이벤트를 발행합니다.
   */
  private void publishPortfolioUpdateEvents(Order order) {
    BigDecimal totalAmount = order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));

    if (order.getIsBuy()) {
      // 매수: 현금 차감, 자산 증가
      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .amount(totalAmount)
              .action(PortfolioUpdateEvent.ActionType.DECREASE_CASH)
              .build()
      );

      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .assetId(order.getAssetId().toString())
              .quantity(BigDecimal.valueOf(order.getQuantity()))
              .action(PortfolioUpdateEvent.ActionType.INCREASE_ASSET)
              .build()
      );
    } else {
      // 매도: 현금 증가, 자산 감소
      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .amount(totalAmount)
              .action(PortfolioUpdateEvent.ActionType.INCREASE_CASH)
              .build()
      );

      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .assetId(order.getAssetId().toString())
              .quantity(BigDecimal.valueOf(order.getQuantity()))
              .action(PortfolioUpdateEvent.ActionType.DECREASE_ASSET)
              .build()
      );
    }
  }

  /**
   * 주문 체결 알림 이벤트를 발행합니다.
   */
  private void publishOrderFilledEvent(Order order) {
    kafkaProducerService.sendOrderFilledEvent(
        OrderFilledEvent.builder()
            .orderId(order.getId())
            .portfolioId(order.getPortfolioId())
            .assetId(order.getAssetId().toString())
            .quantity(BigDecimal.valueOf(order.getQuantity()))
            .price(order.getTargetPrice())
            .executedAt(order.getExecutedAt() != null ?
                order.getExecutedAt().toInstant() :
                Instant.now())
            .build()
    );
  }
}