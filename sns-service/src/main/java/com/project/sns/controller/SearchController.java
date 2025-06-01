package com.project.sns.controller;

import com.project.common.elasticsearch.UserDocument;
import com.project.sns.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/search")
public class SearchController {

    private final SearchService searchService;

    //유저 검색어 입력
    @GetMapping("/user")
    public List<UserDocument> searchUsers(@RequestParam("keyword") String keyword) throws IOException {
        return searchService.searchUsers(keyword);
    }
}
