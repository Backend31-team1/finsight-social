package com.project.sns.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long messageId;
    private String senderNickname;
    private String content;
    private LocalDateTime createdAt;
}
