package com.project.price.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubscriptionService {

  // sessionId → 구독 중인 심볼 집합
  private final Map<String, Set<String>> subs = new ConcurrentHashMap<>();

  /**
   * sessionId가 symbol을 구독하도록 등록
   */
  public void subscribe(String sessionId, String symbol) {
    subs
        .computeIfAbsent(sessionId, id -> ConcurrentHashMap.newKeySet())
        .add(symbol);
  }

  /**
   *  sessionId의 symbol 구독을 해제
   *
   */
  public void unsubscribe(String sessionId, String symbol) {
    Set<String> set = subs.get(sessionId);
    if (set != null) {
      set.remove(symbol);
      if (set.isEmpty()) {
        subs.remove(sessionId);
      }
    }
  }

  /**
   * 해당 sessionId가 구독 중인 심볼 목록 조회
   */
  public Set<String> getSubscribed(String sessionId) {
    return subs.getOrDefault(sessionId, Set.of());
  }

  /**
   * 현재 구독 중인 모든 sessionId 조회 */
  public Set<String> getAllSessionIds() {
    return subs.keySet();
  }
}
