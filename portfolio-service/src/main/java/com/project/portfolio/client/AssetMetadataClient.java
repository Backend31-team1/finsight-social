package com.project.portfolio.client;

import com.project.portfolio.dto.AssetMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "price-service",
    url = "${price-service.url}",
    configuration = com.project.common.config.FeignClientConfiguration.class
)
public interface AssetMetadataClient {

  @GetMapping("/api/meta/{symbol}")
  AssetMetadataDto getOrFetch(@PathVariable("symbol") String symbol);

}
