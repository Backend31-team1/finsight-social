package com.project.price.scheduler;

import com.project.price.dto.QuoteDto;
import com.project.price.service.QuoteService;
import com.project.price.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuotePusher {

  private static final Logger log = LoggerFactory.getLogger(QuotePusher.class);
  private final SubscriptionService subserv;
  private final QuoteService quoteserv;
  private final SimpMessagingTemplate temp;

  private static final long INTERVAL_MS = 15_000L; // 15초

  @Scheduled(fixedDelay = INTERVAL_MS)
  public void pushQuotes() {
    log.info("─── pushQuotes 호출 ───");
    // 모든 활성 세션 ID를 순회
    for (String sessionId : subserv.getAllSessionIds()) {
      // 해당 세션이 구독 중인 심볼 목록

      var symbols = subserv.getSubscribed(sessionId);
      log.info("  session={} 구독목록={}", sessionId, symbols);
      if (symbols.isEmpty()) {
        continue;  // 구독 심볼 없으면 건너뜀
      }
      // 각 심볼별 실시간 시세 조회
      Map<String, QuoteDto> payload = symbols.stream()
          .collect(Collectors.toMap(
              symbol -> symbol,
              quoteserv::fetchQuote
          ));
      log.info("  session={} 에게 전송 payload={}", sessionId, payload);
      // 해당 세션으로 전송
      temp.convertAndSendToUser(
          sessionId,
          "/queue/quotes",
          payload
      );
    }
  }
}
