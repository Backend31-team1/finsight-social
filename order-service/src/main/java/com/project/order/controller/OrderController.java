package com.project.order.controller;

import com.project.order.application.OrderFacade;
import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import jakarta.validation.Valid;
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
}