package com.project.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(
    scanBasePackages = {
        "com.project.auth",        // 이 모듈 패키지
        "com.project.common.config" // common 모듈의 SecurityConfig가 있는 패키지
    }
)
public class AuthApplication {
  public static void main(String[] args){
    SpringApplication.run(AuthApplication.class, args);
  }
}

