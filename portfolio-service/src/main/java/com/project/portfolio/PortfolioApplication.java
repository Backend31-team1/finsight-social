package com.project.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.project.portfolio", "com.project.common.config"})
@EnableFeignClients(basePackages = "com.project.portfolio.client")
public class PortfolioApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioApplication.class, args);
  }
}
