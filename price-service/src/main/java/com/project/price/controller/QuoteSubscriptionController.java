package com.project.price.controller;

import com.project.price.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QuoteSubscriptionController {

  private static final Logger log = LoggerFactory.getLogger(QuoteSubscriptionController.class);
  private final SubscriptionService subscriptionService;

  public static record SubscriptionMsg(String symbol) {}

  /**
   * 구독 시작 (WebSocket)
   /app/price/subscribe
   */
  @MessageMapping("/price/subscribe")
  public void subscribe(SubscriptionMsg msg) {
    String sessionId = msg.symbol(); // 세션 ID를 별도 추출하는 로직이 필요하다면 header 활용
    log.info("> [SUBSCRIBE] symbol={}", msg.symbol());
    subscriptionService.subscribe(sessionId, msg.symbol());
  }

  /**
   * 구독 해제 (WebSocket)
   /app/price/unsubscribe
   */
  @MessageMapping("/price/unsubscribe")
  public void unsubscribe(SubscriptionMsg msg) {
    String sessionId = msg.symbol();
    log.info("X [UNSUBSCRIBE] symbol={}", msg.symbol());
    subscriptionService.unsubscribe(sessionId, msg.symbol());
  }
}
