package com.project.tax.repository;

import com.project.tax.entity.TaxReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxReportRepository extends JpaRepository<TaxReport, Long> {
    Optional<TaxReport> findTopByUserIdOrderByReportedAtDesc(Long userId);
}
