package com.project.tax.client;

import com.project.common.config.FeignClientConfiguration;
import com.project.common.dto.ProfitItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "report-service",
        url = "${REPORT_SERVICE_URL}",
        configuration = FeignClientConfiguration.class
)
public interface ProfitItemClient {

    @GetMapping("/internal/profit-items")
    List<ProfitItemDto> getProfitItemsByUserAndDate(
            @RequestParam("userId") Long userId,
            @RequestParam("start") String startDateTime,
            @RequestParam("end") String endDateTime
    );
}
