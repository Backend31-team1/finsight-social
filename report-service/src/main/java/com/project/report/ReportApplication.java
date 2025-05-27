package com.project.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.project.report","com.project.common"})
public class ReportApplication {
    public static void main(String[] args){
        SpringApplication.run(ReportApplication.class, args);
    }
}
