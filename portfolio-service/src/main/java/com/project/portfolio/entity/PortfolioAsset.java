package com.project.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "portfolio_asset",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_portfolio_symbol", columnNames = {"portfolio_id", "symbol"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long portfolioAssetId;

    @Column(length = 20, nullable = false)
    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
}
