package com.project.sns.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.common.elasticsearch.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    // [유저 검색] 키워드를 기반으로 user 문서(users 인덱스)를 검색
    public List<UserDocument> searchUsers(String keyword) throws IOException {
        SearchResponse<UserDocument> response = elasticsearchClient.search(s -> s
                        .index("users")         // 검색 대상 인덱스
                        .size(50)               // 최대 검색 결과 개수
                        .query(q -> q.bool(b -> b
                                // edge ngram 기반: 접두어 자동완성
                                .should(QueryBuilders.match(m -> m
                                        .field("username.edge")
                                        .query(keyword)
                                        .boost(3.0f)))
                                // 일반 ngram 기반: 중간 문자열 검색 가능
                                .should(QueryBuilders.match(m -> m
                                        .field("username.ngram")
                                        .query(keyword)
                                        .boost(2.0f)))
                                // 표준 분석기로 오타 허용 검색
                                .should(QueryBuilders.match(m -> m
                                        .field("username.standard")
                                        .query(keyword)
                                        .fuzziness("AUTO")
                                        .boost(1.0f)))
                        )),
                UserDocument.class
        );

        // 검색 결과에서 source (UserDocument)만 추출하여 리스트로 반환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
