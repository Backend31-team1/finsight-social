package com.project.order.service;

import com.project.order.dto.OrderRequest;
import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * 시장가 주문 처리 서비스
 *
 * 현재 시장가로 즉시 체결되는 주문을 생성합니다.
 * 외부 의존성 없이 독립적으로 동작합니다.
 */
@Service
@RequiredArgsConstructor
public class MarketOrderService {

  private final OrderRepository orderRepository;

  /**
   * 시장가 주문을 생성합니다.
   *
   * 현재 시장가를 조회하여 즉시 체결 상태로 주문을 생성합니다.
   *
   * @param request 주문 요청 DTO
   * @param userId 주문 생성 사용자 ID (JWT에서 추출)
   * @return 저장된 주문 객체
   */
  public Order placeMarketOrder(OrderRequest request, Long userId) {
    BigDecimal marketPrice = getCurrentMarketPrice(request.getAssetId());

    Order order = Order.builder()
        .userId(userId)
        .assetId(request.getAssetId())
        .portfolioId(request.getPortfolioId())
        .orderType(OrderType.MARKET)
        .quantity(request.getQuantity())
        .targetPrice(marketPrice)
        .status(OrderStatus.FILLED)
        .isBuy(request.getIsBuy())
        .createdAt(Timestamp.from(Instant.now()))
        .executedAt(Timestamp.from(Instant.now()))
        .build();

    return orderRepository.save(order);
  }

  /**
   * 종목의 현재 시장가를 조회합니다.
   *
   * TODO: Price Service의 QuoteService와 연동하여 실제 시세 조회로 대체 예정
   *
   * @param assetId 종목 ID
   * @return 현재 시장가
   */
  private BigDecimal getCurrentMarketPrice(Long assetId) {
    // TODO: Price Service 연동
    // return quoteService.fetchQuote(assetId.toString()).getC(); // current price

    // 임시: 고정 시장가 반환
    return BigDecimal.valueOf(10000);
  }
}