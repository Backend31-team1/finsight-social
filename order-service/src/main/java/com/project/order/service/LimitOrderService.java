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
 * 지정가 주문 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class LimitOrderService {

  private final OrderRepository orderRepository;

  /**
   * 지정가 주문을 생성합니다.
   *
   * @param request - 주문 요청 DTO
   * @return 저장된 주문 객체
   */
  public Order placeLimitOrder(OrderRequest request) {
    Order order = Order.builder()
        .assetId(request.getAssetId())
        .portfolioId(request.getPortfolioId())
        .orderType(OrderType.LIMIT)
        .quantity(request.getQuantity())
        .targetPrice(request.getTargetPrice())
        .status(OrderStatus.PENDING)
        .ttl(request.getTtl())
        .createdAt(Timestamp.from(Instant.now()))
        .build();

    return orderRepository.save(order);
  }
}