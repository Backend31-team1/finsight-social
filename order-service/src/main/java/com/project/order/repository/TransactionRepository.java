package com.project.order.repository;

import com.project.order.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 거래 내역 저장을 위한 JPA 리포지토리 인터페이스입니다.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}