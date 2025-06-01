package com.project.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    // Elasticsearch의 Low-level REST 클라이언트 빈 생성
    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();
    }

    // Elasticsearch Java API 클라이언트 빈 생성
    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        // HTTP 요청을 JSON으로 직렬화/역직렬화
        // Jackson 기반 JSON 직렬화
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }
}
