package com.project.sns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 알림 메시지 전송을 위한 DTO
 *
 * WebSocket 통신을 통해 특정 사용자에게 전송될 알림 내용을 포함합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

  /**
   * 알림 수신자 ID
   */
  private Long receiverId;

  /**
   * 전송할 알림 메시지
   */
  private String message;
}