package com.project.notification.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket을 통해 사용자에게 알림 메시지를 전송하는 컴포넌트입니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationSocketSender {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * 특정 사용자에게 실시간 알림을 전송합니다.
   *
   * @param userId 사용자 ID
   * @param message 전송할 메시지 내용
   */
  public void send(Long userId, String message) {
    String destination = "/queue/notifications";
    messagingTemplate.convertAndSendToUser(userId.toString(), destination, message);
  }
}