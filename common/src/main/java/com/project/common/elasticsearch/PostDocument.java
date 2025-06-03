package com.project.common.elasticsearch;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostDocument {
    private Long id;
    private String title;
}
