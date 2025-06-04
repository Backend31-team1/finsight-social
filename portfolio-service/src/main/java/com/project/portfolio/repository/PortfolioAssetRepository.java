package com.project.portfolio.repository;

import com.project.portfolio.entity.PortfolioAsset;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {

    // 1) 특정 포트폴리오에 속한 모든 PortfolioAsset 조회
    List<PortfolioAsset> findAllByPortfolio_PortfolioId(Long portfolioId);

    @Modifying
    @Query("DELETE FROM PortfolioAsset pa WHERE pa.portfolio.portfolioId = :portfolioId")
    void deleteByPortfolioId(@Param("portfolioId") Long portfolioId);
}
