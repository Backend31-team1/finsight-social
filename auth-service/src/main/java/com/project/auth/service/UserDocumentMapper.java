package com.project.auth.service;

import com.project.auth.entity.User;
import com.project.common.elasticsearch.UserDocument;

public class UserDocumentMapper {
    public static UserDocument fromEntity(User user) {
        return UserDocument.builder()
                .id(user.getId())
                .username(user.getNickname())
                .build();
    }
}
