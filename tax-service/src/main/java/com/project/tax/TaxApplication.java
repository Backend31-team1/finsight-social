package com.project.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.project.tax", "com.project.common.config"})
public class TaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxApplication.class, args);
    }
}
