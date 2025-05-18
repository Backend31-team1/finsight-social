package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.project.price","com.project.common"})
@EnableJpaRepositories(basePackages = {"com.project"})
@EntityScan(basePackages = {"com.project"})
public class PriceApplication {
    public static void main(String[] args){
            SpringApplication.run(PriceApplication.class, args);
        }
}
