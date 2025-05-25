package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.project.price","com.project.common"})
@EnableFeignClients(basePackages = "com.project.price.client")
public class PriceApplication {
    public static void main(String[] args){
            SpringApplication.run(PriceApplication.class, args);
        }
}
