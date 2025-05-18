package com.project.order.repository;

import com.project.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문 저장소
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}