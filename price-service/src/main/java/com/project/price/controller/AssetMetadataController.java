package com.project.price.controller;

import com.project.price.entity.AssetMetadata;
import com.project.price.service.AssetMetadataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class AssetMetadataController {

  private final AssetMetadataService service;

  /**
   * 1. 심볼로 회사 메타데이터 조회 (풀텍스트 검색만 가능) - DB에 있으면 바로 반환 - 없으면 API 호출 후 저장 → 반환
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
   * 2. 이름 또는 심볼 키워드로 검색 - DB에 저장된 종목 중에서만 검색 - 이 부분은 구현에 대해서 어떻게 구현해야 할지 좀더 고민해볼 필요가 있지만, 일단 DB에 저장되어 있는 회사의 메타 정보들에서만
   * 가죠오는 형태로 구현
   */
  @GetMapping("/search")
  public ResponseEntity<List<AssetMetadata>> search(
      @RequestParam("q") String keyword
  ) {
    List<AssetMetadata> results = service.search(keyword);
    return ResponseEntity.ok(results);
  }
}