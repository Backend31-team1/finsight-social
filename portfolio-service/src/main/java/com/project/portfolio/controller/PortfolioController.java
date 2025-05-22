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
}
