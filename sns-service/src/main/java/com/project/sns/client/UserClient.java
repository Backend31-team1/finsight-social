package com.project.sns.client;

import com.project.common.config.FeignClientConfiguration;
import com.project.common.dto.UserSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${AUTH_SERVICE_URL}",
        configuration = FeignClientConfiguration.class
)
public interface UserClient {
    @GetMapping("/auth/users/{userId}")
    UserSummaryDto getUserSummary(@PathVariable("userId") Long userId);
}
