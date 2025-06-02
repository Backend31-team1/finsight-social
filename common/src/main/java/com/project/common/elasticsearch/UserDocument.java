package com.project.common.elasticsearch;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDocument {
    private Long id;
    private String username;
}
