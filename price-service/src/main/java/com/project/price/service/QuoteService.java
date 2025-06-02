package com.project.price.service;



import com.project.price.client.FinnhubClient;
import com.project.price.dto.QuoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteService {

  private final FinnhubClient finnhubClient;

  @Value("${finnhub.api-key}")
  private String apiKey;

  /**
   * 심볼 하나에 대해 Finnhub에서 실시간 시세를 조회하여 DTO로 반환
   */
  public QuoteDto fetchQuote(String symbol) {
    return finnhubClient.getQuote(symbol, apiKey);
  }
}
