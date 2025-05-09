package com.project.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.project.auth","com.project.common"})
public class AuthApplication {
  public static void main(String[] args){
    SpringApplication.run(AuthApplication.class, args);
  }
}
