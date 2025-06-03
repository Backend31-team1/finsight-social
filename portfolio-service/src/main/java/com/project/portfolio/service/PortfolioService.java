package com.project.portfolio.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.portfolio.dto.PortfolioCreationDto;
import com.project.portfolio.dto.PortfolioDetailResponseDto;
import com.project.portfolio.dto.PortfolioResponseDto;
import com.project.portfolio.entity.Portfolio;
import com.project.portfolio.entity.PortfolioAsset;
import com.project.portfolio.repository.PortfolioAssetRepository;
import com.project.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioAssetRepository portfolioAssetRepository;

    @Transactional
    public void createPortfolio(Long userId, PortfolioCreationDto request) {
        // 포트폴리오 생성
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setName(request.getName());
        portfolio.setCreatedAt(LocalDateTime.now());

        portfolioRepository.save(portfolio);
    }

    // 사용자의 포트폴리오 전체 조회
    @Transactional(readOnly = true)
    public List<PortfolioResponseDto> getSimplePortfolios(Long userId) {
        return portfolioRepository.findAllByUserId(userId).stream()
                .map(p -> new PortfolioResponseDto(p.getPortfolioId(), p.getName()))
                .toList();
    }

    // 포트폴리오 삭제
    @Transactional
    public void deletePortfolio(Long userId, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByPortfolioIdAndUserId(portfolioId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUNT_PORTFOLIO));

        // 자식 삭제
        portfolioAssetRepository.deleteByPortfolioId(portfolioId);

        // 부모 삭제
        portfolioRepository.delete(portfolio);
    }

    // 포트폴리오 상세 조회
    @Transactional(readOnly = true)
    public PortfolioDetailResponseDto getPortfolioDetail(Long userId, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByPortfolioIdAndUserId(portfolioId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUNT_PORTFOLIO));

        List<Long> assetIds = portfolioAssetRepository.findByPortfolioId(portfolioId).stream()
                .map(PortfolioAsset::getPortfolioAssetId)
                .toList();

        return PortfolioDetailResponseDto.builder()
                .portfolioId(portfolio.getPortfolioId())
                .name(portfolio.getName())
                .assetIds(assetIds)
                .build();
    }
}
