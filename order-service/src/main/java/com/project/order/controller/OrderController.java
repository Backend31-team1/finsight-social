package com.project.order.controller;

import com.project.common.UserVo;
import com.project.order.application.OrderFacade;
import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import com.project.order.service.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 API 컨트롤러
 *
 * 가상 주식 거래 시스템의 주문 처리를 담당합니다.
 * - 시장가/지정가 주문 처리
 * - 주문 내역 조회 및 관리
 * - 주문 취소 기능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "주문 API", description = "주식 주문 처리 및 조회 API")
public class OrderController {

  private final OrderFacade orderFacade;
  private final OrderQueryService orderQueryService;

  /**
   * 주문을 처리합니다.
   *
   * orderType 값에 따라 시장가(MARKET) 또는 지정가(LIMIT) 주문으로 분기됩니다.
   * - 시장가: 현재 시장가로 즉시 체결
   * - 지정가: 지정된 가격에 대기 상태로 등록
   *
   * @param request 주문 요청 정보 (종목ID, 포트폴리오ID, 수량, 주문타입, 매수/매도 구분)
   * @param user 인증된 사용자 정보
   * @return 처리된 주문 정보
   */
  @Operation(summary = "주문 처리",
      description = "시장가 또는 지정가 주문을 처리합니다. orderType=MARKET(시장가)/LIMIT(지정가), isBuy=true(매수)/false(매도)")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "주문 처리 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 주문 정보"),
      @ApiResponse(responseCode = "401", description = "인증 필요")
  })
  @PostMapping
  public ResponseEntity<OrderResponseDto> placeOrder(
      @RequestBody @Valid OrderRequest request,
      @AuthenticationPrincipal UserVo user
  ) {
    OrderResponseDto response;

    if ("MARKET".equalsIgnoreCase(request.getOrderType())) {
      response = orderFacade.placeMarketOrder(request, user.getId());
    } else if ("LIMIT".equalsIgnoreCase(request.getOrderType())) {
      response = orderFacade.placeLimitOrder(request, user.getId());
    } else {
      throw new IllegalArgumentException("지원하지 않는 주문 타입: " + request.getOrderType());
    }

    return ResponseEntity.ok(response);
  }

  /**
   * 포트폴리오별 주문 내역을 조회합니다.
   *
   * 해당 포트폴리오의 모든 주문 내역을 최신순으로 반환합니다.
   * 사용자 권한 검증을 위해 userId도 함께 확인합니다.
   *
   * @param portfolioId 조회할 포트폴리오 ID
   * @param user 인증된 사용자 정보
   * @return 주문 내역 리스트
   */
  @Operation(summary = "주문 내역 조회", description = "특정 포트폴리오의 모든 주문 내역을 조회합니다")
  @GetMapping("/history")
  public ResponseEntity<List<OrderResponseDto>> getOrderHistory(
      @RequestParam Long portfolioId,
      @AuthenticationPrincipal UserVo user
  ) {
    List<OrderResponseDto> orders = orderQueryService.getOrdersByUserAndPortfolio(user.getId(), portfolioId);
    return ResponseEntity.ok(orders);
  }

  /**
   * 특정 주문의 상세 정보를 조회합니다.
   *
   * @param orderId 조회할 주문 ID
   * @param user 인증된 사용자 정보
   * @return 주문 상세 정보
   */
  @Operation(summary = "주문 상세 조회", description = "주문 ID로 특정 주문의 상세 정보를 조회합니다")
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponseDto> getOrder(
      @PathVariable Long orderId,
      @AuthenticationPrincipal UserVo user
  ) {
    OrderResponseDto order = orderQueryService.getOrderByIdAndUserId(orderId, user.getId());
    return ResponseEntity.ok(order);
  }

  /**
   * 대기 중인 주문을 취소합니다.
   *
   * PENDING 상태의 주문만 취소 가능하며, 이미 체결되었거나 취소된 주문은 취소할 수 없습니다.
   *
   * @param orderId 취소할 주문 ID
   * @param user 인증된 사용자 정보
   * @return 성공 시 204 No Content
   */
  @Operation(summary = "주문 취소", description = "PENDING 상태의 주문을 취소합니다")
  @DeleteMapping("/{orderId}")
  public ResponseEntity<Void> cancelOrder(
      @PathVariable Long orderId,
      @AuthenticationPrincipal UserVo user
  ) {
    orderFacade.cancelOrder(orderId, user.getId());
    return ResponseEntity.noContent().build();
  }
}