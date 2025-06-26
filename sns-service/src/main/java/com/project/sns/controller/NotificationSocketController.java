// NotificationSocketController.java
package com.project.sns.controller;

import com.project.sns.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationSocketController {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * 대상 사용자에게 실시간 알림 전송
   * (topic 경로를 /topic/sns/notifications/{receiverId} 로 통일)
   */
  public void sendNotification(Long receiverId, String content) {
    messagingTemplate.convertAndSend(
        "/topic/sns/notifications/" + receiverId,
        new NotificationMessage(receiverId, content)
    );
  }
}
