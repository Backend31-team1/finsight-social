package com.project.order.application;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.order.dto.OrderRequest;
import com.project.order.dto.OrderResponseDto;
import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.repository.OrderRepository;
import com.project.order.service.ExecutionService;
import com.project.order.service.LimitOrderService;
import com.project.order.service.MarketOrderService;
import com.project.order.service.OrderQueryService;
import com.project.order.service.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 전체 흐름을 처리하는 application 계층 (의존성 제거 버전)
 *
 * 외부 서비스 의존성 없이 순수하게 주문 생성/관리 로직만 담당합니다.
 * 포트폴리오 관련 검증은 이벤트 기반으로 처리됩니다.
 */
@Service
@RequiredArgsConstructor
public class OrderFacade {

  private final MarketOrderService marketOrderService;
  private final LimitOrderService limitOrderService;
  private final ExecutionService executionService;
  private final OrderQueryService orderQueryService;
  private final OrderValidationService orderValidationService;
  private final OrderRepository orderRepository;

  /**
   * 시장가 주문을 처리하고 즉시 체결합니다.
   *
   * @param request 주문 요청 DTO
   * @param userId 주문 생성 사용자 ID (JWT에서 추출)
   * @return 체결된 주문 정보
   */
  @Transactional
  public OrderResponseDto placeMarketOrder(OrderRequest request, Long userId) {
    // 기본 유효성 검증 (외부 의존성 없음)
    orderValidationService.validateMarketOrder(request);

    // 주문 생성 및 저장
    Order order = marketOrderService.placeMarketOrder(request, userId);

    // 즉시 체결 처리 (포트폴리오 업데이트 이벤트 발행)
    executionService.processExecution(order);

    return orderQueryService.convertToDto(order);
  }

  /**
   * 지정가 주문을 등록합니다.
   *
   * @param request 지정가 주문 요청 DTO
   * @param userId 주문 생성 사용자 ID (JWT에서 추출)
   * @return 등록된 주문 정보 (상태: PENDING)
   */
  @Transactional
  public OrderResponseDto placeLimitOrder(OrderRequest request, Long userId) {
    // 기본 유효성 검증 (외부 의존성 없음)
    orderValidationService.validateLimitOrder(request);

    // 지정가 주문 생성 및 저장 (PENDING 상태)
    Order order = limitOrderService.placeLimitOrder(request, userId);

    return orderQueryService.convertToDto(order);
  }

  /**
   * 주문을 취소합니다.
   *
   * @param orderId 취소할 주문 ID
   * @param userId 주문 취소를 요청한 사용자 ID
   */
  @Transactional
  public void cancelOrder(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRANSACTION));

    // userId 기반 권한 검증 (간단함)
    orderValidationService.validateCancelPermission(order, userId);

    // 취소 가능 상태 확인
    if (order.getStatus() != OrderStatus.PENDING) {
      throw new CustomException(ErrorCode.CANNOT_CANCEL_ORDER);
    }

    // 주문 상태를 CANCELLED로 변경
    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);
  }
}