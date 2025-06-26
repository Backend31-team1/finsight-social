package com.project.portfolio.controller;

import com.project.common.UserVo;
import com.project.portfolio.dto.PortfolioCreationDto;
import com.project.portfolio.dto.PortfolioResponseDto;
import com.project.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    /**
     * 포트폴리오 생성
     * POST /api/portfolio/create
     */
    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(
        @AuthenticationPrincipal UserVo user,
        @RequestBody PortfolioCreationDto request
    ) {
        portfolioService.createPortfolio(user.getId(), request);
        return ResponseEntity.ok("포트폴리오 생성 성공");
    }

    /**
     * 포트폴리오 리스트 조회
     * GET /api/portfolio/list
     */
    @GetMapping("/list")
    public ResponseEntity<List<PortfolioResponseDto>> getSimplePortfolioList(
        @AuthenticationPrincipal UserVo user
    ) {
        List<PortfolioResponseDto> result = portfolioService.getSimplePortfolios(user.getId());
        return ResponseEntity.ok(result);
    }

    /**
     * 포트폴리오 삭제
     * DELETE /api/portfolio/{portfolioId}
     */
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(
        @AuthenticationPrincipal UserVo user,
        @PathVariable Long portfolioId
    ) {
        portfolioService.deletePortfolio(user.getId(), portfolioId);
        return ResponseEntity.ok().build();
    }
}
