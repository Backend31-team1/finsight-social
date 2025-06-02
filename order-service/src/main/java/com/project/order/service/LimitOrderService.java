package com.project.order.service;

import com.project.order.dto.OrderRequest;
import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * 지정가 주문 저장을 처리하는 서비스입니다.
 * 체결 조건 만족 시 추후 ExecutionService에서 체결 처리됩니다.
 */
@Service
@RequiredArgsConstructor
public class LimitOrderService {

  private final OrderRepository orderRepository;

  /**
   * 지정가 주문을 저장합니다. (PENDING 상태로 저장)
   *
   * @param request - 지정가 주문 요청 DTO
   * @return 저장된 주문 객체
   */
  public Order placeLimitOrder(OrderRequest request) {
    Order order = Order.builder()
        .portfolioId(request.getPortfolioId())
        .assetId(request.getAssetId())
        .quantity(request.getQuantity())
        .targetPrice(request.getTargetPrice())
        .orderType(OrderType.LIMIT)
        .status(OrderStatus.PENDING) // 체결은 추후 실행됨
        .createdAt(Timestamp.from(Instant.now()))
        .build();

    return orderRepository.save(order);
  }
}