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
 *
 * 지정된 가격에 대기 상태로 주문을 생성합니다.
 * 체결은 LimitOrderScheduler에서 별도로 처리됩니다.
 */
@Service
@RequiredArgsConstructor
public class LimitOrderService {

  private final OrderRepository orderRepository;

  /**
   * 지정가 주문을 저장합니다. (PENDING 상태로 저장)
   *
   * @param request 지정가 주문 요청 DTO
   * @param userId 주문 생성 사용자 ID (JWT에서 추출)
   * @return 저장된 주문 객체
   */
  public Order placeLimitOrder(OrderRequest request, Long userId) {
    Order order = Order.builder()
        .userId(userId)
        .portfolioId(request.getPortfolioId())
        .assetId(request.getAssetId())
        .quantity(request.getQuantity())
        .targetPrice(request.getTargetPrice())
        .orderType(OrderType.LIMIT)
        .status(OrderStatus.PENDING) // 체결은 추후 스케줄러에서 처리
        .isBuy(request.getIsBuy())
        .ttl(request.getTtl())
        .createdAt(Timestamp.from(Instant.now()))
        .build();

    return orderRepository.save(order);
  }
}