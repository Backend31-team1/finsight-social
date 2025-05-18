package com.project.price.repository;

import com.project.price.entity.CashAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {

    Optional<CashAccount> findByUserId(Long userId);
}
