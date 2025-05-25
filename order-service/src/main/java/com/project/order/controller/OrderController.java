package com.project.order.controller;

import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import com.project.order.entity.Order;
import com.project.order.service.LimitOrderService;
import com.project.order.service.MarketOrderService;
import com.project.order.service.OrderQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 시장가 주문 API 컨트롤러
 *
 * @author ㅇㅇ
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final MarketOrderService marketOrderService;
  private final LimitOrderService limitOrderService;
  private final OrderQueryService orderQueryService;

  /**
   * 시장가 주문을 생성합니다.
   *
   * @param request - 주문 요청 정보
   * @return 생성된 주문 정보
   */
  @PostMapping("/market")
  public ResponseEntity<Order> placeMarketOrder(@RequestBody OrderRequest request) {
    Order order = marketOrderService.placeMarketOrder(request);
    return ResponseEntity.ok(order);
  }

  /**
   * 지정가 주문을 생성합니다.
   *
   * @param request - 주문 요청 정보
   * @return 생성된 주문 정보
   */
  @PostMapping("/limit")
  public ResponseEntity<Order> placeLimitOrder(@RequestBody OrderRequest request) {
    Order order = limitOrderService.placeLimitOrder(request);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam Long portfolioId) {
    List<OrderResponseDto> orders = orderQueryService.getOrdersByPortfolioId(portfolioId);
    return ResponseEntity.ok(orders);
  }
}