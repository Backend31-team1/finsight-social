package com.project.order.application;

import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import com.project.order.entity.Order;
import com.project.order.service.ExecutionService;
import com.project.order.service.MarketOrderService;
import com.project.order.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 전체 흐름을 처리하는 application 계층
 */
@Service
@RequiredArgsConstructor
public class OrderFacade {

  private final MarketOrderService marketOrderService;
  private final ExecutionService executionService;
  private final OrderQueryService orderQueryService;

  /**
   * 시장가 주문 요청을 처리하고 체결까지 수행합니다.
   *
   * @param request - 주문 요청 DTO
   * @return 주문 응답 DTO
   */
  @Transactional
  public OrderResponseDto placeMarketOrder(OrderRequest request) {
    // 주문 저장
    Order order = marketOrderService.placeMarketOrder(request);

    // 주문 체결 처리
    executionService.processExecution(order);

    // 응답 DTO로 변환
    return orderQueryService.convertToDto(order);
  }
}