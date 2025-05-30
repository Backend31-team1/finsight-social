package com.project.sns.service;

import com.project.common.dto.UserSummaryDto;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.sns.client.UserClient;
import com.project.sns.dto.ChatRoomSummaryResponse;
import com.project.sns.entity.ChatParticipant;
import com.project.sns.entity.ChatRoom;
import com.project.sns.entity.Message;
import com.project.sns.repository.ChatParticipantRepository;
import com.project.sns.repository.ChatRoomRepository;
import com.project.sns.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserClient userClient;

    //내 채팅방 조회
    public List<ChatRoomSummaryResponse> getMyChatRooms(Long myUserId) {
        List<Long> roomIds = chatParticipantRepository.findRoomIdsByUserId(myUserId);

        return roomIds.stream().map(roomId -> {
            ChatRoom room = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

            // 상대 유저 ID 조회
            Long otherUserId = chatParticipantRepository.findByChatRoom_RoomId(roomId).stream()
                    .map(ChatParticipant::getUserId)
                    .filter(id -> !id.equals(myUserId))
                    .findFirst().orElse(null);

            // Feign으로 상대 유저 정보 가져오기
            UserSummaryDto userDto = userClient.getUserSummary(otherUserId);

            // 마지막 메시지 조회
            String lastMessage = messageRepository.findTopByRoom_RoomIdOrderByCreatedAtDesc(roomId)
                    .map(Message::getContent)
                    .orElse("");

            return ChatRoomSummaryResponse.builder()
                    .roomId(room.getRoomId())
                    .createdAt(room.getCreatedAt())
                    .otherUserNickname(userDto.getNickname())
                    .lastMessage(lastMessage)
                    .build();
        }).toList();
    }
}
