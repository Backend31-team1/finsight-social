package com.project.order.controller;

import com.project.order.dto.OrderRequest;
import com.project.order.entity.Order;
import com.project.order.service.LimitOrderService;
import com.project.order.service.MarketOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  private final MarketOrderService marketOrderService;
  private final LimitOrderService limitOrderService;

  /**
   * 시장가 주문을 생성합니다.
   *
   * POST /api/order/market
   */
  @PostMapping("/market")
  public ResponseEntity<Order> placeMarketOrder(@RequestBody OrderRequest request) {
    Order order = marketOrderService.placeMarketOrder(request);
    return ResponseEntity.ok(order);
  }

  /**
   * 지정가 주문을 생성합니다.
   *
   * POST /api/order/limit
   */
  @PostMapping("/limit")
  public ResponseEntity<Order> placeLimitOrder(@RequestBody OrderRequest request) {
    Order order = limitOrderService.placeLimitOrder(request);
    return ResponseEntity.ok(order);
  }
}
