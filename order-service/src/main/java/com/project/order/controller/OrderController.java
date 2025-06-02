package com.project.order.controller;

import com.project.order.application.OrderFacade;
import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import com.project.order.service.OrderQueryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderFacade orderFacade;
  private final OrderQueryService orderQueryService;


  /**
   * 시장가 주문 요청을 처리합니다.
   *
   * @param request - 주문 요청 DTO
   * @return 주문 응답 DTO
   */
  @PostMapping("/market")
  public ResponseEntity<OrderResponseDto> placeMarketOrder(@RequestBody @Valid OrderRequest request) {
    return ResponseEntity.ok(orderFacade.placeMarketOrder(request));
  }

  /**
   * 포트폴리오 ID에 해당하는 모든 주문 내역을 조회합니다.
   *
   * @param portfolioId - 주문 조회 대상 포트폴리오 ID
   * @return 주문 응답 DTO 리스트
   */
  @GetMapping("/history")
  public ResponseEntity<List<OrderResponseDto>> getOrderHistory(@RequestParam Long portfolioId) {
    List<OrderResponseDto> orders = orderQueryService.getOrdersByPortfolioId(portfolioId);
    return ResponseEntity.ok(orders);
  }
}