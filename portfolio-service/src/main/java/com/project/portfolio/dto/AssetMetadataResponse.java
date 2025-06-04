package com.project.portfolio.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AssetMetadataResponse {
  private String symbol;
  private String name;
  private String exchange;
  private String country;
  private String finnhubIndustry;
  private Long marketCapitalization;
  private Double shareOutstanding;
  private String logo;
  private String weburl;
  private LocalDate ipo;
  private String updatedAt;

  // AssetMetadataDto를 기반으로 응답 DTO 생성
  public AssetMetadataResponse(AssetMetadataDto dto) {
    this.symbol = dto.getSymbol();
    this.name = dto.getName();
    this.exchange = dto.getExchange();
    this.country = dto.getCountry();
    this.finnhubIndustry = dto.getFinnhubIndustry();
    this.marketCapitalization = dto.getMarketCapitalization();
    this.shareOutstanding = dto.getShareOutstanding();
    this.logo = dto.getLogo();
    this.weburl = dto.getWeburl();
    this.ipo = dto.getIpo();
    this.updatedAt = dto.getUpdatedAt();
  }
}
