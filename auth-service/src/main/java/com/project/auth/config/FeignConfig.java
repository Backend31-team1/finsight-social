package com.project.auth.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  @Value("${mailgun.api.key}")
  private String mailgunKey;

  @Qualifier(value = "mailgun")
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
    return new BasicAuthRequestInterceptor("api",mailgunKey);
  }

}
