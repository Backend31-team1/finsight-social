package com.project.price.repository;

import com.project.price.entity.CashAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {

    List<CashAccount> findAllByUserId(Long userId);

}
