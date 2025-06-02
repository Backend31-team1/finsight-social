package com.project.price.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // 클라이언트가 여기로 WS 연결을 시도
    registry.addEndpoint("/ws/quotes")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 클라이언트→서버 메시지는 /app prefix
    registry.setApplicationDestinationPrefixes("/app");
    // 서버→클라이언트 푸시는 /queue prefix
    registry.enableSimpleBroker("/queue");
    registry.setUserDestinationPrefix("/user");
  }
}
