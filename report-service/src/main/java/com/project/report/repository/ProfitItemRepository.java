package com.project.report.repository;

import com.project.report.entity.ProfitItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProfitItemRepository extends JpaRepository<ProfitItem, Long> {
    List<ProfitItem> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
