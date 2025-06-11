package com.project.tax.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Auth Service 전용 Swagger 설정
 * 각 모듈에서 개별적으로 작성하고,
 * 이후 API Gateway나 문서 통합 서비스에서 통합을 진행.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Tax Service API",
        version = "v1.0.0",
        description = "세금 관련 API 문서",
        contact = @Contact(
            name = "김재혁",
            email = "jaehyeok.ethan@gmail.com"
        )
    )
)
@Configuration
public class OpenApiConfig {
}
