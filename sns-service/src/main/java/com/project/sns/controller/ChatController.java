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

    // 메세지 전송
    @MessageMapping("/chat.send")
    public void handleChatMessage(ChatMessageRequest request) {
        ChatMessageResponse response = messageService.saveAndBuildResponse(request);

        // 구독 중인 사람들에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
    }
}
