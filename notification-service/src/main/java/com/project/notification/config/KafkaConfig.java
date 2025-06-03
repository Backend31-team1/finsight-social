package com.project.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Consumer 설정 클래스입니다.
 * - order.filled 토픽 메시지를 수신하기 위한 Consumer 설정을 정의합니다.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

  /**
   * Kafka ConsumerFactory 빈 정의
   * - Kafka broker에 연결하고 메시지를 역직렬화할 설정을 포함합니다.
   */
  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // 도커 환경의 Kafka 주소
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  /**
   * Kafka Listener Container Factory 정의
   * - 실제 KafkaListener가 사용할 listener factory를 구성합니다.
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}