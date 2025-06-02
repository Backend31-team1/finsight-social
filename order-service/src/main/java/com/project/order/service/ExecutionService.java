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
import java.time.Instant;

/**
 * 주문 체결 처리 및 체결 후 후속 작업을 담당하는 서비스입니다.
 * - 현금/자산 처리
 * - 거래 내역 저장
 * - Kafka 이벤트 발행
 */
@Service
@RequiredArgsConstructor
public class ExecutionService {

  private final OrderRepository orderRepository;
  private final KafkaProducerService kafkaProducerService;
  private final TransactionService transactionService;

  /**
   * 주문 체결 처리 메서드입니다.
   * 체결 완료 시 거래 내역을 저장하고 Kafka 이벤트를 발행합니다.
   *
   * @param order - 체결된 주문 객체
   */
  @Transactional
  public void processExecution(Order order) {

    order.setStatus(OrderStatus.FILLED); // 주문 상태 업데이트
    orderRepository.save(order);         // DB 반영

    transactionService.saveTransaction(order); // 거래 내역 저장

    BigDecimal totalAmount = order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));

    if (order.getOrderType().isBuy()) {
      // 💸 매수: 현금 차감, 자산 증가
      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .amount(totalAmount)
              .action(PortfolioUpdateEvent.ActionType.DECREASE_CASH)
              .build()
      );

      // 포트폴리오 반영 메시지 발행
      kafkaProducerService.sendPortfolioUpdateEvent(
          PortfolioUpdateEvent.builder()
              .portfolioId(order.getPortfolioId())
              .assetId(order.getAssetId().toString())
              .quantity(BigDecimal.valueOf(order.getQuantity()))
              .action(PortfolioUpdateEvent.ActionType.INCREASE_ASSET)
              .build()
      );

    } else {
      // 💰 매도: 현금 증가, 자산 감소
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

    // Kafka로 체결 이벤트 발행
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