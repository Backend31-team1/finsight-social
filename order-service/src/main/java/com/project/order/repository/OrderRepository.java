package com.project.order.repository;

import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 저장소
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * 특정 포트폴리오 ID에 해당하는 모든 주문을 조회합니다.
   *
   * @param portfolioId - 포트폴리오 ID
   * @return 주문 리스트
   */
  List<Order> findByPortfolioId(Long portfolioId);

  List<Order> findByOrderTypeAndStatus(OrderType orderType, OrderStatus status);
}