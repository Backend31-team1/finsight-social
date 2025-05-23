package com.project.price.controller;

import com.project.price.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class QuoteSubscriptionController {

  private static final Logger log = LoggerFactory.getLogger(QuoteSubscriptionController.class);
  private final SubscriptionService subscriptionService;

  public static record SubscriptionMsg(String symbol) {}

  /**
   * 클라이언트가 /app/subscribe 로 보내는 메시지를 처리
   */
  @MessageMapping("/subscribe")
  public void subscribe(SubscriptionMsg msg, SimpMessageHeaderAccessor header) {
    String sessionId = header.getSessionId();
    log.info("> [SUBSCRIBE] session={} symbol={}", sessionId, msg.symbol());
    subscriptionService.subscribe(sessionId, msg.symbol());
  }

  /**
   * 클라이언트가 /app/unsubscribe 로 보내는 메시지를 처리
   * 프론트개발시에는 “뒤로 가기”나 “컴포넌트 언마운트” 시점에 이 /app/unsubscribe를 호출하도록 구현하면 될 것 같습니당.
   */
  @MessageMapping("/unsubscribe")
  public void unsubscribe(SubscriptionMsg msg, SimpMessageHeaderAccessor header) {
    String sessionId = header.getSessionId();
    log.info("X [UNSUBSCRIBE] session={} symbol={}", sessionId, msg.symbol());
    subscriptionService.unsubscribe(sessionId, msg.symbol());
  }
}

