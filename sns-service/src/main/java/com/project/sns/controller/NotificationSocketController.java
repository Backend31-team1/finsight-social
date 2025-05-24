package com.project.sns.controller;

import com.project.sns.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * WebSocket 알림 메시지 발송 컨트롤러
 *
 * 댓글 작성 등 이벤트 발생 시, 대상 사용자에게 실시간으로 STOMP 메시지를 전송합니다.
 */
@Controller
@RequiredArgsConstructor
public class NotificationSocketController {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * 사용자에게 실시간 알림 전송
   *
   * @param receiverId - 알림 수신 대상 사용자 ID
   * @param content - 전송할 알림 메시지 내용
   */
  public void sendNotification(Long receiverId, String content) {
    messagingTemplate.convertAndSend(
        "/topic/notifications/" + receiverId,
        new NotificationMessage(receiverId, content)
    );
  }
}