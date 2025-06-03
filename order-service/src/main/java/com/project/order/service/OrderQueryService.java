package com.project.order.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.order.dto.OrderResponseDto;
import com.project.order.entity.Order;
import com.project.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 조회 전용 서비스
 *
 * 주문 내역 조회와 관련된 읽기 전용 작업을 처리합니다.
 * - 사용자별 주문 조회
 * - 포트폴리오별 주문 조회
 * - 주문 상세 정보 조회
 * - 엔티티-DTO 변환
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

  private final OrderRepository orderRepository;

  /**
   * 특정 사용자의 특정 포트폴리오 주문을 조회합니다.
   *
   * 권한 검증을 위해 userId와 portfolioId를 모두 확인합니다.
   *
   * @param userId 사용자 ID
   * @param portfolioId 포트폴리오 ID
   * @return 주문 응답 DTO 리스트
   */
  public List<OrderResponseDto> getOrdersByUserAndPortfolio(Long userId, Long portfolioId) {
    List<Order> orders = orderRepository.findByUserIdAndPortfolioIdOrderByCreatedAtDesc(userId, portfolioId);

    return orders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * 특정 포트폴리오의 모든 주문을 조회합니다.
   *
   * 생성일시 기준 최신순으로 정렬하여 반환합니다.
   * (기존 호환성을 위해 유지)
   *
   * @param portfolioId 포트폴리오 ID
   * @return 주문 응답 DTO 리스트
   */
  public List<OrderResponseDto> getOrdersByPortfolioId(Long portfolioId) {
    List<Order> orders = orderRepository.findByPortfolioId(portfolioId);

    return orders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * 특정 사용자의 모든 주문을 조회합니다.
   *
   * @param userId 사용자 ID
   * @return 주문 응답 DTO 리스트
   */
  public List<OrderResponseDto> getOrdersByUserId(Long userId) {
    List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

    return orders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * 주문 ID로 특정 주문을 조회합니다.
   *
   * @param orderId 조회할 주문 ID
   * @return 주문 응답 DTO
   * @throws CustomException 존재하지 않는 주문일 때
   */
  public OrderResponseDto getOrderById(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRANSACTION));

    return convertToDto(order);
  }

  /**
   * 주문 ID와 사용자 ID로 주문을 조회합니다. (권한 검증 포함)
   *
   * @param orderId 조회할 주문 ID
   * @param userId 사용자 ID
   * @return 주문 응답 DTO
   * @throws CustomException 존재하지 않는 주문이거나 권한이 없을 때
   */
  public OrderResponseDto getOrderByIdAndUserId(Long orderId, Long userId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRANSACTION));

    // 권한 검증
    if (!order.getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    return convertToDto(order);
  }

  /**
   * Order 엔티티를 OrderResponseDto로 변환합니다.
   *
   * 클라이언트에게 반환할 DTO 형태로 필요한 정보만 매핑합니다.
   *
   * @param order 변환할 Order 엔티티
   * @return OrderResponseDto
   */
  public OrderResponseDto convertToDto(Order order) {
    return OrderResponseDto.builder()
        .id(order.getId())
        .portfolioId(order.getPortfolioId())
        .assetId(order.getAssetId().toString())
        .orderType(order.getOrderType())
        .quantity(BigDecimal.valueOf(order.getQuantity()))
        .targetPrice(order.getTargetPrice())
        .status(order.getStatus())
        .createdAt(order.getCreatedAt())
        .executedAt(order.getExecutedAt())
        .build();
  }
}