package com.project.price.controller;

import com.project.price.dto.QuoteDto;
import com.project.price.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price/quote")
@RequiredArgsConstructor
public class QuoteController {

  private final QuoteService quoteService;

  /**
   * 실시간 시세 조회
   * GET /api/price/quote/{symbol}
   */
  @GetMapping("/{symbol}")
  public ResponseEntity<QuoteDto> getQuote(@PathVariable String symbol) {
    return ResponseEntity.ok(quoteService.fetchQuote(symbol));
  }
}
