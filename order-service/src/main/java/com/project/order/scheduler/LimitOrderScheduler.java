package com.project.order.scheduler;

import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import com.project.order.repository.OrderRepository;
import com.project.order.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 지정가 주문 관리 스케줄러
 *
 * PENDING 상태의 지정가 주문들을 주기적으로 체크하여 체결 조건 확인 및 TTL 만료 처리를 수행합니다.
 * - 체결 조건 만족 시 자동 체결
 * - TTL 만료 시 자동 취소
 */
@Component
@RequiredArgsConstructor
public class LimitOrderScheduler {

  private final OrderRepository orderRepository;
  private final ExecutionService executionService;

  /**
   * PENDING 상태의 지정가 주문을 체결 조건에 따라 처리합니다.
   *
   * 10초마다 실행되며, 현재 시장가와 지정가를 비교하여 체결 여부를 결정합니다.
   * - 매수: 현재가 ≤ 지정가일 때 체결
   * - 매도: 현재가 ≥ 지정가일 때 체결
   */
  @Scheduled(fixedDelay = 10000)
  public void executePendingLimitOrders() {
    List<Order> pendingOrders = orderRepository.findByOrderTypeAndStatus(OrderType.LIMIT, OrderStatus.PENDING);

    for (Order order : pendingOrders) {
      BigDecimal currentPrice = getMockMarketPrice(order.getAssetId().toString());

      boolean isExecutable = false;

      if (order.getIsBuy()) {
        // 매수: 현재 가격이 지정가 이하이면 체결
        isExecutable = currentPrice.compareTo(order.getTargetPrice()) <= 0;
      } else {
        // 매도: 현재 가격이 지정가 이상이면 체결
        isExecutable = currentPrice.compareTo(order.getTargetPrice()) >= 0;
      }

      if (isExecutable) {
        // 현재 시장가로 체결가격 업데이트
        order.setTargetPrice(currentPrice);
        executionService.processExecution(order);
      }
    }
  }

  /**
   * TTL이 만료된 주문들을 취소 처리합니다.
   *
   * 30초마다 실행되며, 개선된 Repository 메서드를 사용하여 만료된 주문을 효율적으로 조회합니다.
   */
  @Scheduled(fixedDelay = 30000)
  public void cancelExpiredOrders() {
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    List<Order> expiredOrders = orderRepository.findExpiredPendingOrders(currentTime);

    for (Order order : expiredOrders) {
      order.setStatus(OrderStatus.CANCELLED);
      orderRepository.save(order);
    }
  }

  /**
   * 실제 가격 서비스와 연동 전까지 사용하는 모의 시장가 생성 메서드
   *
   * TODO: Price Service의 QuoteService와 연동하여 실제 시장가 조회로 대체 예정
   *
   * @param assetId 자산 ID
   * @return 현재 시장 가격 (임시: 9500~10500 사이의 랜덤값)
   */
  private BigDecimal getMockMarketPrice(String assetId) {
    // 임시로 9500 ~ 10500 사이의 랜덤 가격 생성
    int randomPrice = 9500 + (int)(Math.random() * 1000);
    return new BigDecimal(randomPrice);
  }
}