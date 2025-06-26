// ChatRoomController.java
package com.project.sns.controller;

import com.project.sns.dto.ChatRoomCreateRequest;
import com.project.sns.dto.ChatRoomResponse;
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
@RequestMapping("/api/sns/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    /**
     * GET /api/sns/chat/rooms : 내 채팅방 전체 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getMyChatRooms(@RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

    /**
     * GET /api/sns/chat/room/{roomId}/messages : 특정 채팅방 메시지 조회
     */
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<Page<MessageResponse>> getMessages(
        @PathVariable Long roomId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(messageService.getMessagesByRoom(roomId, page, size));
    }

    /**
     * POST /api/sns/chat/room : 채팅방 생성 또는 조회
     */
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(
        @RequestBody ChatRoomCreateRequest request
    ) {
        return ResponseEntity.ok(chatRoomService.createOrGetRoom(request));
    }
}
