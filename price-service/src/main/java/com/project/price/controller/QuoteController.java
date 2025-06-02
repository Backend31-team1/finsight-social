package com.project.price.controller;

import com.project.price.dto.QuoteDto;
import com.project.price.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quote")
@RequiredArgsConstructor
public class QuoteController {
  private final QuoteService quoteService;

  @GetMapping("/{symbol}")
  public ResponseEntity<QuoteDto> getQuote(@PathVariable String symbol) {
    return ResponseEntity.ok(quoteService.fetchQuote(symbol));
  }
}
