package com.project.portfolio.controller;

import com.project.common.UserVo;
import com.project.portfolio.dto.AssetMetadataResponse;
import com.project.portfolio.service.PortfolioAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/portfolio/{portfolioId}/assets")
public class PortfolioAssetController {

  private final PortfolioAssetService assetService;

  /**
   * 포트폴리오에 종목 추가
   * Body: { "symbol": "AAPL" }
   */
  @PostMapping
  public ResponseEntity<Void> addAsset(
      @AuthenticationPrincipal UserVo user,
      @PathVariable Long portfolioId,
      @RequestBody Map<String, String> body // {"symbol": "AAPL"}
  ) {
    String symbol = body.get("symbol");
    assetService.addAssetToPortfolio(user.getId(), portfolioId, symbol);
    return ResponseEntity.ok().build();
  }

  /**
   * 포트폴리오에 담긴 종목 전체 조회
   */
  @GetMapping
  public ResponseEntity<List<AssetMetadataResponse>> listAssets(
      @AuthenticationPrincipal UserVo user,
      @PathVariable Long portfolioId
  ) {
    List<AssetMetadataResponse> assetList =
        assetService.listAssetsInPortfolio(user.getId(), portfolioId);
    return ResponseEntity.ok(assetList);
  }

  /**
   * 3) 포트폴리오에서 특정 종목 제거
   */
  @DeleteMapping("/{portfolioAssetId}")
  public ResponseEntity<Void> removeAsset(
      @AuthenticationPrincipal UserVo user,
      @PathVariable Long portfolioId,
      @PathVariable Long portfolioAssetId
  ) {
    assetService.removeAssetFromPortfolio(user.getId(), portfolioAssetId);
    return ResponseEntity.ok().build();
  }
}

