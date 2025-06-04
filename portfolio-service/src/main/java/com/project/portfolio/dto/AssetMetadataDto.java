package com.project.portfolio.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//feing 요청결과 반환타입 받는 Dto

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetMetadataDto {

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

}
