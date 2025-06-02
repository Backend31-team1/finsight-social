package com.project.auth.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.config.ElasticsearchConfig;
import com.project.common.elasticsearch.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMigrationService {

    private final UserRepository userRepository;
    private final ElasticsearchClient elasticsearchClient;

    //유저 마이그레이션
    public void migrateUsersToElasticsearch() throws IOException {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            UserDocument doc = UserDocumentMapper.fromEntity(user);
            elasticsearchClient.index(i -> i
                    .index("users")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        }
    }
}
