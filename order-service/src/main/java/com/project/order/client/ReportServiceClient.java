package com.project.order.client;

import com.project.common.config.FeignClientConfiguration;
import com.project.order.dto.ProfitItemCreateRequest;
import com.project.order.dto.ProfitItemCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Report Service와 통신하여 ProfitItem을 생성하는 Feign Client입니다.
 */
@FeignClient(
    name = "report-service",
    url = "http://localhost:8086",
    configuration = FeignClientConfiguration.class
)
public interface ReportServiceClient {

  /**
   * 매도 거래 시 ProfitItem을 생성합니다.
   *
   * @param request ProfitItem 생성 요청
   * @return 생성된 ProfitItem 정보
   */
  @PostMapping("/auth/internal/profit-items")
  ProfitItemCreateResponse createProfitItem(@RequestBody ProfitItemCreateRequest request);
}