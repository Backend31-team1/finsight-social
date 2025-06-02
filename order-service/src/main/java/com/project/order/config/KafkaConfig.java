package com.project.order.config;

import com.project.order.dto.OrderFilledEvent;
import com.project.order.dto.PortfolioUpdateEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * KafkaProducer를 위한 커스텀 설정 클래스입니다.
 * 이벤트 타입별 KafkaTemplate을 Bean으로 등록합니다.
 */
@Configuration
public class KafkaConfig {

  private Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 주소
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return props;
  }

  @Bean
  public ProducerFactory<String, OrderFilledEvent> orderFilledProducerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, OrderFilledEvent> orderFilledKafkaTemplate() {
    return new KafkaTemplate<>(orderFilledProducerFactory());
  }

  @Bean
  public ProducerFactory<String, PortfolioUpdateEvent> portfolioUpdateProducerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, PortfolioUpdateEvent> portfolioUpdateKafkaTemplate() {
    return new KafkaTemplate<>(portfolioUpdateProducerFactory());
  }
}