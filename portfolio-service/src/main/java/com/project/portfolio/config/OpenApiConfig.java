package com.project.portfolio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Portfolio Service API",
                version = "v1.0.0",
                description = "포트폴리오 API 문서",
                contact = @Contact(
                        name = "고희재",
                        email = "rhgmlwo11@gmail.com"
                )
        )
)
@Configuration
public class OpenApiConfig {
}

