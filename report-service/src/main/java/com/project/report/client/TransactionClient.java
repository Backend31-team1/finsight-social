package com.project.report.client;

import com.project.common.config.FeignClientConfiguration;
import com.project.common.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "price-service",
        url = "http://localhost:8085",
        configuration = FeignClientConfiguration.class
)
public interface TransactionClient {
    @PostMapping("/auth/transaction/batch")
    List<TransactionDto> getTransactionsByIds(@RequestBody List<Long> transactionIds);
}
