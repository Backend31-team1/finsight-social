package com.project.price.client;

import com.project.common.config.FeignClientConfiguration;
import com.project.common.dto.UserIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8081",
        configuration = FeignClientConfiguration.class
)
public interface UserClient {

    @GetMapping("/auth/users/{userId}")
    UserIdDto getUserId(@PathVariable("userId") Long userId);
}
