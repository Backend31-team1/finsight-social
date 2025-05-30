package com.project.sns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateRequest {
    private Long myUserId;
    private Long targetUserId;
}
