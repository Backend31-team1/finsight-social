package com.project.sns.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomSummaryResponse {
    private Long roomId;
    private LocalDateTime createdAt;
    private String otherUserNickname;
    private String lastMessage;
}
