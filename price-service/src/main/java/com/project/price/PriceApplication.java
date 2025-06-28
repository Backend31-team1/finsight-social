package com.project.price;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.project.price", "com.project.common.config"})
@EnableFeignClients(basePackages = "com.project.price.client")
@EnableScheduling
public class PriceApplication {
    public static void main(String[] args){
            SpringApplication.run(PriceApplication.class, args);
        }
}
