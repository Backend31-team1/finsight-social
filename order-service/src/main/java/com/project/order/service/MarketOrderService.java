package com.project.order.service;

import com.project.order.dto.OrderRequest;
import com.project.order.entity.*;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * 시장가 주문 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class MarketOrderService {

  private final OrderRepository orderRepository;

  /**
   * 시장가 주문을 생성합니다.
   *
   * @param request - 주문 요청 DTO
   * @return 저장된 주문 객체
   */
  public Order placeMarketOrder(OrderRequest request) {
    BigDecimal marketPrice = getMarketPrice(request.getAssetId()); // 실시간 시세 연동 예정

    Order order = Order.builder()
        .assetId(request.getAssetId())
        .portfolioId(request.getPortfolioId())
        .orderType(OrderType.MARKET)
        .quantity(request.getQuantity())
        .targetPrice(marketPrice)
        .status(OrderStatus.FILLED)
        .createdAt(Timestamp.from(Instant.now()))
        .executedAt(Timestamp.from(Instant.now()))
        .build();

    return orderRepository.save(order);
  }

  /**
   * 종목의 시장가를 가져옵니다 (임시 값)
   *
   * @param assetId - 종목 ID
   * @return BigDecimal 시장가
   */
  private BigDecimal getMarketPrice(Long assetId) {
    return BigDecimal.valueOf(9870); // TODO: price-service 연동 예정
  }
}