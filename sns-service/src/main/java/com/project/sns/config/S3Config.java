package com.project.sns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
  @Value("${spring.cloud.aws.credentials.access-key}")
  private String accessKey;
  @Value("${spring.cloud.aws.credentials.secret-key}")
  private String secretKey;
  @Value("${spring.cloud.aws.region.static}")
  private String region;

  @Bean
  public S3Client s3Client(){
    AwsBasicCredentials awsCred = AwsBasicCredentials.create(accessKey, secretKey);
    return S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(awsCred))
        .region(Region.of(region))
        .build();
  }

}
