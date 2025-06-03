package com.project.portfolio.controller;

import com.project.common.UserVo;
import com.project.portfolio.dto.PortfolioCreationDto;
import com.project.portfolio.dto.PortfolioDetailResponseDto;
import com.project.portfolio.dto.PortfolioResponseDto;
import com.project.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    // 포트폴리오 생성
    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(
            @AuthenticationPrincipal UserVo user,
            @RequestBody PortfolioCreationDto request
    ) {
        portfolioService.createPortfolio(user.getId(), request);
        return ResponseEntity.ok("포트폴리오 생성 성공");
    }

    // 포트폴리오 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<PortfolioResponseDto>> getSimplePortfolioList(
            @AuthenticationPrincipal UserVo user
    ) {
        List<PortfolioResponseDto> result = portfolioService.getSimplePortfolios(user.getId());
        return ResponseEntity.ok(result);
    }

    // 포트폴리오 삭제
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(
            @AuthenticationPrincipal UserVo user,
            @PathVariable Long portfolioId
    ) {
        portfolioService.deletePortfolio(user.getId(), portfolioId);
        return ResponseEntity.ok().build();
    }

    // 포트폴리오 상세 조회
    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDetailResponseDto> getPortfolioDetail(
            @AuthenticationPrincipal UserVo user,
            @PathVariable Long portfolioId
    ) {
        PortfolioDetailResponseDto result = portfolioService.getPortfolioDetail(user.getId(), portfolioId);
        return ResponseEntity.ok(result);
    }
}
