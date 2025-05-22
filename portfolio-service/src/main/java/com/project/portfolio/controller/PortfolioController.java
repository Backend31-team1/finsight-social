package com.project.portfolio.controller;

import com.project.common.UserVo;
import com.project.portfolio.dto.PortfolioCreationDto;
import com.project.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(
            @AuthenticationPrincipal UserVo userDetails,
            @RequestBody PortfolioCreationDto request
    ) {
        portfolioService.createPortfolio(userDetails.getId(), request);
        return ResponseEntity.ok("포트폴리오 생성 성공");
    }
}
