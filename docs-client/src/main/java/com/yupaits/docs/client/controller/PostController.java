package com.yupaits.docs.client.controller;

import com.yupaits.docs.common.constants.DocsConsts;
import com.yupaits.docs.common.dto.PostCreate;
import com.yupaits.docs.common.dto.PostQuery;
import com.yupaits.docs.common.dto.PostUpdate;
import com.yupaits.docs.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author yupaits
 * @date 2018/8/21
 */
@RestController
public class PostController {

    private final OAuth2RestTemplate restTemplate;

    @Autowired
    public PostController(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/docs/page")
    public Result getDocsResource(@RequestBody PostQuery postQuery, @PageableDefault Pageable pageable) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/page?page={page}&size={size}";
        return restTemplate.postForObject(url, postQuery, Result.class, pageable.getPageNumber(), pageable.getPageSize());
    }

    @GetMapping("/docs/options")
    public Result getPostOptions() {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/options";
        return restTemplate.getForObject(url, Result.class);
    }

    @GetMapping("/docs/{postId}")
    public Result getPost(@PathVariable Long postId) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/{postId}";
        return restTemplate.getForObject(url, Result.class, postId);
    }

    @PostMapping("/docs")
    public Result addPost(@RequestBody PostCreate postCreate) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs";
        return restTemplate.postForObject(url, postCreate, Result.class);
    }

    @DeleteMapping("/docs/{postId}")
    public Result deletePost(@PathVariable Long postId) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/{postId}";
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Result.class, postId).getBody();
    }

    @PutMapping("/docs/{postId}")
    public Result updatePost(@PathVariable Long postId, @RequestBody PostUpdate postUpdate) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/{postId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<PostUpdate> entity = new HttpEntity<>(postUpdate, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, Result.class, postId).getBody();
    }
}
