package com.project.order.scheduler;

import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import com.project.order.repository.OrderRepository;
import com.project.order.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 지정가 주문 체결 여부를 주기적으로 확인하고 체결하는 스케줄러입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LimitOrderScheduler {

  private final OrderRepository orderRepository;
  private final ExecutionService executionService;

  /**
   * 10초마다 PENDING 상태의 지정가 주문을 체결 조건에 따라 처리합니다.
   */
  @Scheduled(fixedDelay = 10000) // 10초마다 실행
  public void executePendingLimitOrders() {
    List<Order> pendingOrders = orderRepository.findByOrderTypeAndStatus(OrderType.LIMIT, OrderStatus.PENDING);

    for (Order order : pendingOrders) {
      BigDecimal currentPrice = getMockMarketPrice(order.getAssetId().toString());

      boolean isExecutable = order.getTargetPrice().compareTo(currentPrice) >= 0;
      if (isExecutable) {
        log.info("💡 체결 조건 만족 → 주문 ID {} 실행", order.getId());
        executionService.processExecution(order);
      }
    }
  }

  /**
   * 실제 가격 서비스와 연동 전까지는 모의 가격 사용
   *
   * @param assetId - 자산 ID
   * @return 현재 시장 가격
   */
  private BigDecimal getMockMarketPrice(String assetId) {
    return new BigDecimal("10000"); // 실제 price-service 연동 시 대체
  }
}