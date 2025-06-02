package com.project.order.service;

import com.project.order.dto.OrderResponseDto;
import com.project.order.entity.Order;
import com.project.order.repository.OrderRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 내역 조회를 위한 서비스
 */
@Service
@RequiredArgsConstructor
public class OrderQueryService {

  private final OrderRepository orderRepository;

  /**
   * 특정 포트폴리오 ID의 모든 주문을 조회합니다.
   *
   * @param portfolioId - 포트폴리오 ID
   * @return 주문 응답 DTO 리스트
   */
  public List<OrderResponseDto> getOrdersByPortfolioId(Long portfolioId) {
    List<Order> orders = orderRepository.findByPortfolioId(portfolioId);
    return orders.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

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
