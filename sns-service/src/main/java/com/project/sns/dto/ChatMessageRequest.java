package com.project.sns.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long roomId;
    private Long senderId;
    private String content;
}
