package com.project.sns.controller;

import com.project.sns.dto.ChatRoomSummaryResponse;
import com.project.sns.dto.MessageResponse;
import com.project.sns.service.ChatRoomService;
import com.project.sns.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    // 내 채팅방 전체 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getMyChatRooms(@RequestParam Long userId) {
        List<ChatRoomSummaryResponse> rooms = chatRoomService.getMyChatRooms(userId);
        return ResponseEntity.ok(rooms);
    }

    // 특정 채팅방 메세지 조회
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<Page<MessageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MessageResponse> messages = messageService.getMessagesByRoom(roomId, page, size);
        return ResponseEntity.ok(messages);
    }
}
