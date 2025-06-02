package com.project.price.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileDto {
  private String country;
  private String currency;
  private String exchange;

  @JsonProperty("finnhubIndustry")
  private String finnhubIndustry;

  private String ipo;
  private String logo;

  @JsonProperty("marketCapitalization")
  private Long marketCapitalization;

  private String name;
  private Double shareOutstanding;
  private String ticker;
  private String weburl;
}
