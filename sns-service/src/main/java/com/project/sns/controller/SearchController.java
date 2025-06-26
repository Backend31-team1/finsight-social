// SearchController.java
package com.project.sns.controller;

import com.project.common.elasticsearch.PostDocument;
import com.project.common.elasticsearch.UserDocument;
import com.project.sns.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sns/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * GET /api/sns/search/user?keyword={keyword}
     */
    @GetMapping("/user")
    public List<UserDocument> searchUsers(@RequestParam("keyword") String keyword) throws IOException {
        return searchService.searchUsers(keyword);
    }

    /**
     * GET /api/sns/search/post?keyword={keyword}
     */
    @GetMapping("/post")
    public List<PostDocument> searchPosts(@RequestParam("keyword") String keyword) throws IOException {
        return searchService.searchPosts(keyword);
    }
}
