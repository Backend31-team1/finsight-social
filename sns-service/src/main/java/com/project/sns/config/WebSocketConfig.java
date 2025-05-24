package com.project.sns.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocket 설정 클래스
 *
 * STOMP 프로토콜 기반 WebSocket 통신을 위한 엔드포인트 및 메시지 브로커 설정을 담당합니다.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /**
   * 메시지 브로커 설정
   *
   * @param registry - 메시지 브로커 등록 객체
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic"); // 구독 주소
    registry.setApplicationDestinationPrefixes("/app"); // 발행 주소 prefix
  }

  /**
   * WebSocket 연결 엔드포인트 등록
   *
   * @param registry - STOMP 엔드포인트 등록 객체
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-notification") // WebSocket 연결 주소
        .setAllowedOriginPatterns("*")
        .withSockJS(); // fallback 대응용
  }
}