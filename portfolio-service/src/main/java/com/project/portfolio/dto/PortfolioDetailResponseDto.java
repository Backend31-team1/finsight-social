package com.project.portfolio.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PortfolioDetailResponseDto {
    private Long portfolioId;
    private String name;
    private List<Long> assetIds;
}
