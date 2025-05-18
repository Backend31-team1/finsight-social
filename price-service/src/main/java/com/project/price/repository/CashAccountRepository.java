package com.project.price.repository;

import com.project.price.entity.CashAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {
}
