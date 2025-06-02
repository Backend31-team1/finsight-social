package com.project.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDto {
    private Long userId;
    private String nickname;
}
