package com.project.price.client;

import com.project.price.dto.CompanyProfileDto;
import com.project.price.dto.QuoteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "finnhub", url = "${finnhub.base-url}")
public interface FinnhubClient {

  @GetMapping("/stock/profile2")
  CompanyProfileDto getProfile(
      @RequestParam("symbol") String symbol,
      @RequestParam("token") String apiKey
  );

  @GetMapping("/quote")
  QuoteDto getQuote(
      @RequestParam("symbol") String symbol,
      @RequestParam("token") String apiKey
  );

}
