package com.project.sns.controller;

import com.project.common.UserVo;
import com.project.sns.dto.NotificationResponseDto;
import com.project.sns.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 알림 관련 API를 처리하는 컨트롤러
 *
 * SNS Service 내의 알림 기능을 담당합니다.
 * - 댓글, 좋아요 등 소셜 기능 관련 알림
 * - 주문 체결 알림은 별도 Notification Service에서 처리
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * 사용자의 모든 알림을 조회합니다.
   *
   * 생성시간 기준 최신순으로 정렬하여 반환합니다.
   *
   * @param user 인증된 사용자 정보
   * @return 알림 목록
   */
  @GetMapping
  public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
      @AuthenticationPrincipal UserVo user
  ) {
    List<NotificationResponseDto> notifications = notificationService.getUserNotifications(user.getId());
    return ResponseEntity.ok(notifications);
  }

  /**
   * 특정 알림을 읽음 처리합니다.
   *
   * @param id 알림 ID
   * @param user 인증된 사용자 정보
   * @return 성공 메시지
   */
  @PatchMapping("/{id}/read")
  public ResponseEntity<String> markAsRead(
      @PathVariable("id") Long id,
      @AuthenticationPrincipal UserVo user
  ) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok("알림 읽음 처리 완료");
  }
}