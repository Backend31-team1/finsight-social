package com.project.order.repository;

import com.project.order.entity.Order;
import com.project.order.entity.OrderStatus;
import com.project.order.entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 주문 데이터 접근을 위한 JPA Repository
 *
 * 주문 조회, 필터링 및 TTL 관리 기능을 제공합니다.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * 특정 포트폴리오 ID에 해당하는 모든 주문을 조회합니다.
   *
   * @param portfolioId 포트폴리오 ID
   * @return 주문 리스트
   */
  List<Order> findByPortfolioId(Long portfolioId);

  /**
   * 특정 주문 타입과 상태에 해당하는 주문들을 조회합니다.
   *
   * @param orderType 주문 타입
   * @param status 주문 상태
   * @return 주문 리스트
   */
  List<Order> findByOrderTypeAndStatus(OrderType orderType, OrderStatus status);

  /**
   * 특정 포트폴리오들의 주문을 조회합니다.
   *
   * @param portfolioIds 포트폴리오 ID 리스트
   * @return 주문 리스트
   */
  List<Order> findByPortfolioIdIn(List<Long> portfolioIds);

  /**
   * TTL이 만료된 PENDING 주문들을 조회합니다.
   *
   * 현재 시간과 주문 생성 시간의 차이가 TTL보다 큰 주문들을 찾습니다.
   * 복잡한 EPOCH 계산 대신 단순한 Timestamp 비교를 사용합니다.
   *
   * @param currentTime 현재 시간
   * @return TTL이 만료된 주문 리스트
   */
  @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.ttl IS NOT NULL " +
      "AND ((:currentTime - o.createdAt) > o.ttl)")
  List<Order> findExpiredPendingOrders(@Param("currentTime") Timestamp currentTime);

  /**
   * 특정 자산에 대한 주문들을 조회합니다.
   *
   * @param assetId 자산 ID
   * @return 주문 리스트
   */
  List<Order> findByAssetId(Long assetId);

  /**
   * 특정 상태의 모든 주문을 조회합니다.
   *
   * @param status 주문 상태
   * @return 주문 리스트
   */
  List<Order> findByStatus(OrderStatus status);

  /**
   * 특정 사용자의 주문을 조회합니다.
   *
   * @param userId 사용자 ID
   * @return 사용자의 주문 리스트
   */
  List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

  /**
   * 특정 사용자의 특정 포트폴리오 주문을 조회합니다.
   *
   * @param userId 사용자 ID
   * @param portfolioId 포트폴리오 ID
   * @return 주문 리스트
   */
  List<Order> findByUserIdAndPortfolioIdOrderByCreatedAtDesc(Long userId, Long portfolioId);
}