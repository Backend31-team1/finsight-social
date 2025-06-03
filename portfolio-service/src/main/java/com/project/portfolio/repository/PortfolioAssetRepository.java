package com.project.portfolio.repository;

import com.project.portfolio.entity.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAssetRepository, Long> {
    @Modifying
    @Query("DELETE FROM PortfolioAsset pa WHERE pa.portfolio.portfolioId = :portfolioId")
    void deleteByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT pa FROM PortfolioAsset pa WHERE pa.portfolio.portfolioId = :portfolioId")
    List<PortfolioAsset> findByPortfolioId(@Param("portfolioId") Long portfolioId);
}
