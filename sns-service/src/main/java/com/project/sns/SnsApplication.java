package com.project.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.project.sns", "com.project.common"})
public class SnsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SnsApplication.class, args);
  }
}