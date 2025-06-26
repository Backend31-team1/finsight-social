package com.project.price.controller;

import com.project.price.entity.AssetMetadata;
import com.project.price.service.AssetMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price/metadata")
@RequiredArgsConstructor
public class AssetMetadataController {

  private final AssetMetadataService service;

  /**
   * 심볼로 회사 메타데이터 조회
   * GET /api/price/metadata/{symbol}
   */
  @GetMapping("/{symbol}")
  public ResponseEntity<AssetMetadata> getBySymbol(
      @PathVariable String symbol,
      @RequestHeader(value = "Authorization", required = false) String authHeader
  ) {
    AssetMetadata meta = service.getOrFetch(symbol);
    return ResponseEntity.ok(meta);
  }

  /**
   * 이름 또는 심볼 키워드로 검색
   * GET /api/price/metadata/search?q={keyword}
   */
  @GetMapping("/search")
  public ResponseEntity<List<AssetMetadata>> search(
      @RequestParam("q") String keyword
  ) {
    List<AssetMetadata> results = service.search(keyword);
    return ResponseEntity.ok(results);
  }
}
