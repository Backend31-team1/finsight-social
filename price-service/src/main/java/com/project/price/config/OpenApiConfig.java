package com.project.price.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Price Service API",
                version = "v1.0.0",
                description = "계좌 및 종목 관련 API 문서",
                contact = @Contact(
                        name = "고희재",
                        email = "rhgmlwo11@gmail.com"
                )
        )
)
@Configuration
public class OpenApiConfig {
}
