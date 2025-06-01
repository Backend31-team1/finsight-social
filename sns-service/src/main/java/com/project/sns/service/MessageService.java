package com.project.sns.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.sns.client.UserClient;
import com.project.sns.dto.ChatMessageRequest;
import com.project.sns.dto.ChatMessageResponse;
import com.project.sns.dto.MessageResponse;
import com.project.sns.entity.ChatRoom;
import com.project.sns.entity.Message;
import com.project.sns.repository.ChatRoomRepository;
import com.project.sns.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserClient userClient;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 메세지 조회
    public Page<MessageResponse> getMessagesByRoom(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        return messageRepository.findByRoom_RoomIdOrderByCreatedAtAsc(roomId, pageable)
                .map(message -> {
                    String nickname = "";
                    try {
                        nickname = userClient.getUserSummary(message.getSenderId()).getNickname();
                    } catch (Exception e) {
                        nickname = "(알 수 없음)";
                    }

                    return MessageResponse.builder()
                            .messageId(message.getMessageId())
                            .senderNickname(nickname)
                            .content(message.getContent())
                            .createdAt(message.getCreatedAt())
                            .build();
                });
    }

    // 메세지 전송
    public ChatMessageResponse saveAndBuildResponse(ChatMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        Message saved = messageRepository.save(Message.builder()
                .room(room)
                .senderId(request.getSenderId())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        String nickname = "";
        try {
            nickname = userClient.getUserSummary(saved.getSenderId()).getNickname();
        } catch (Exception e) {
            nickname = "(알 수 없음)";
        }

        return ChatMessageResponse.builder()
                .messageId(saved.getMessageId())
                .senderNickname(nickname)
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
