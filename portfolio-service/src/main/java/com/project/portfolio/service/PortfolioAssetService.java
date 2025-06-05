package com.project.portfolio.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.portfolio.client.AssetMetadataClient;
import com.project.portfolio.dto.AssetMetadataResponse;
import com.project.portfolio.entity.Portfolio;
import com.project.portfolio.entity.PortfolioAsset;
import com.project.portfolio.repository.PortfolioAssetRepository;
import com.project.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioAssetService {

  private final PortfolioRepository portfolioRepository;
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final AssetMetadataClient assetMetadataClient;

  // 포트폴리오에 symbol 추가
  @Transactional
  public void addAssetToPortfolio(Long userId, Long portfolioId, String symbol) {
    Portfolio portfolio = portfolioRepository
        .findByPortfolioIdAndUserId(portfolioId, userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PORTFOLIO));
    // 심볼 중복시 예외
    boolean alreadyExists = portfolioAssetRepository
        .existsByPortfolio_PortfolioIdAndSymbol(portfolioId, symbol);
    if (alreadyExists) {
      throw new CustomException(ErrorCode.ASSET_ALREADY_EXIST);
    }


    assetMetadataClient.getOrFetch(symbol); // price모듈에 요청 날림

    PortfolioAsset asset = PortfolioAsset.builder()
        .symbol(symbol)
        .portfolio(portfolio)
        .build();

    portfolioAssetRepository.save(asset);
  }

  // 하나의 포트폴리오에 담긴 종목 전체 조회 (메타데이터 포함해서 조회하도록 구현)
  @Transactional(readOnly = true)
  public List<AssetMetadataResponse> listAssetsInPortfolio(Long userId, Long portfolioId) {
    Portfolio portfolio = portfolioRepository
        .findByPortfolioIdAndUserId(portfolioId, userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PORTFOLIO));

    List<String> symbols = portfolioAssetRepository.findAllByPortfolio_PortfolioId(portfolioId)
        .stream()
        .map(PortfolioAsset::getSymbol)
        .collect(Collectors.toList());

    return symbols.stream()
        .map(assetMetadataClient::getOrFetch)     // AssetMetadataDto 반환
        .map(dto -> new AssetMetadataResponse(dto)) // DTO → UI용 응답 DTO 변환
        .collect(Collectors.toList());
  }

  // 3) 포트폴리오에서 개별 종목 제거
  @Transactional
  public void removeAssetFromPortfolio(Long userId, Long portfolioAssetId) {
    PortfolioAsset asset = portfolioAssetRepository
        .findById(portfolioAssetId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ASSET));

    if (!asset.getPortfolio().getUserId().equals(userId)) {
      throw new CustomException(ErrorCode.FORBIDDEN);
    }

    portfolioAssetRepository.delete(asset);
  }
}
