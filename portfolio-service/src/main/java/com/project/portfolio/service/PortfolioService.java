package com.project.portfolio.service;

import com.project.portfolio.dto.PortfolioCreationDto;
import com.project.portfolio.dto.PortfolioResponseDto;
import com.project.portfolio.entity.Portfolio;
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
}
