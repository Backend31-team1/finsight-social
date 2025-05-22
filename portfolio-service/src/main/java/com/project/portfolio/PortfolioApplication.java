package com.project.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.project.portfolio","com.project.common"})
public class PortfolioApplication {
    public static void main(String[] args){
            SpringApplication.run(PortfolioApplication.class, args);
        }
}
