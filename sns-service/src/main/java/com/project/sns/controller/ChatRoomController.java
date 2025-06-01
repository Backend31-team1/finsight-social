package com.project.sns.controller;

import com.project.sns.dto.ChatRoomSummaryResponse;
import com.project.sns.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 내 채팅방 전체 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getMyChatRooms(@RequestParam Long userId) {
        List<ChatRoomSummaryResponse> rooms = chatRoomService.getMyChatRooms(userId);
        return ResponseEntity.ok(rooms);
    }
}
