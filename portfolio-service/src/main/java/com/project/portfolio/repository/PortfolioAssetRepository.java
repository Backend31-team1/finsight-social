package com.project.portfolio.repository;

import com.project.portfolio.entity.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    @Modifying
    @Query("DELETE FROM PortfolioAsset pa WHERE pa.portfolio.portfolioId = :portfolioId")
    void deleteByPortfolioId(@Param("portfolioId") Long portfolioId);
}
