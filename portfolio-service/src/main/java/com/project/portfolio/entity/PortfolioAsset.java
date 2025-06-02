package com.project.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "portfolio_asset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioAsset {
    @Id
    private Long portfolioAssetId;

    private Long assetId;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
}
