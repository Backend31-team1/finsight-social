package com.project.sns.service;

import com.project.common.elasticsearch.PostDocument;
import com.project.sns.entity.Post;

public class PostDocumentMapper {

    public static PostDocument fromEntity(Post post) {
        return PostDocument.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .build();
    }
}
