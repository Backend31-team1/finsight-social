package com.project.sns.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.project.common.elasticsearch.PostDocument;
import com.project.sns.entity.Post;
import com.project.sns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMigrationService {

    private final PostRepository postRepository;
    private final ElasticsearchClient elasticsearchClient;

    // 게시글 마이그레이션
    public void migratePostsToElasticsearch() throws IOException {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {
            PostDocument doc = PostDocumentMapper.fromEntity(post);
            elasticsearchClient.index(i -> i
                    .index("posts")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        }
    }
}
