package com.project.portfolio.client;

import com.project.portfolio.dto.AssetMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "price-service",
    url = "${PRICE_SERVICE_URL}",
    configuration = com.project.common.config.FeignClientConfiguration.class
)
public interface AssetMetadataClient {

  @GetMapping("/api/metadata/{symbol}")
  AssetMetadataDto getOrFetch(@PathVariable("symbol") String symbol);

}
