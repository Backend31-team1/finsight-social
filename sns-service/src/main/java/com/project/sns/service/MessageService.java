package com.project.sns.service;

import com.project.sns.client.UserClient;
import com.project.sns.dto.MessageResponse;
import com.project.sns.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserClient userClient;

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
}
