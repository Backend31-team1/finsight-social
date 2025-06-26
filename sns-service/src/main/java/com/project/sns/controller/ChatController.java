// ChatController.java
package com.project.sns.controller;

import com.project.sns.dto.ChatMessageRequest;
import com.project.sns.dto.ChatMessageResponse;
import com.project.sns.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * STOMP 메시지 수신: /app/sns/chat/send
     */
    @MessageMapping("/sns/chat/send")
    public void handleChatMessage(ChatMessageRequest request) {
        ChatMessageResponse response = messageService.saveAndBuildResponse(request);
        // 구독 중인 사람들에게 메시지 전송
        messagingTemplate.convertAndSend(
            "/topic/sns/room/" + request.getRoomId(),
            response
        );
    }
}
