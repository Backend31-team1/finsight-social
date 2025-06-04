package com.project.price.service;

import com.project.price.client.FinnhubClient;
import com.project.price.dto.CompanyProfileDto;
import com.project.price.entity.AssetMetadata;
import com.project.price.repository.AssetMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetMetadataService {

  private final FinnhubClient finnhubClient;
  private final AssetMetaDataRepository repository;

  @Value("${finnhub.api-key}")
  private String apiKey;

  /**
   * DB에 있으면 반환 , 없으면 API 호출 후 저장
   */
  public AssetMetadata getOrFetch(String symbol) {
    return repository.findById(symbol)
        .orElseGet(() -> fetchAndSave(symbol));
  }

  /**
   * 반드시 API 호출 -> 저장(업데이트)
   */
  public AssetMetadata fetchAndSave(String symbol) {
    CompanyProfileDto dto = finnhubClient.getProfile(symbol, apiKey);

    AssetMetadata meta = new AssetMetadata();
    meta.setSymbol(dto.getTicker());
    meta.setName(dto.getName());
    meta.setExchange(dto.getExchange());
    meta.setCountry(dto.getCountry());
    meta.setFinnhubIndustry(dto.getFinnhubIndustry());
    meta.setMarketCapitalization(dto.getMarketCapitalization());
    meta.setShareOutstanding(dto.getShareOutstanding());
    meta.setLogo(dto.getLogo());
    meta.setWeburl(dto.getWeburl());
    if (dto.getIpo() != null && !dto.getIpo().isBlank()) {
      meta.setIpo(LocalDate.parse(dto.getIpo()));
    }
    //Jpa save에 의해 id로 식별후 중복없이 같은값이면 갱신 아니면 추가 됌.
    return repository.save(meta);
  }

  /**
   * DB에 저장된 종목 중 이름 또는 심볼에 키워드가 포함된 전체 리스트 반환
   */
  public List<AssetMetadata> search(String keyword) {
    return repository.findByNameContainingIgnoreCaseOrSymbolContaining(keyword, keyword);
  }
}
