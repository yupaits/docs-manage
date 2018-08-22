package com.yupaits.docs.client.controller;

import com.yupaits.docs.common.constants.DocsConsts;
import com.yupaits.docs.common.dto.PostCateCreate;
import com.yupaits.docs.common.dto.PostCateUpdate;
import com.yupaits.docs.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author yupaits
 * @date 2018/8/22
 */
@RestController
public class PostCateController {

    private final OAuth2RestTemplate restTemplate;

    @Autowired
    public PostCateController(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/docs/cate/list")
    public Result listDocsCate() {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/cate/list";
        return restTemplate.getForObject(url, Result.class);
    }

    @PostMapping("/docs/cate")
    public Result addDocsCate(@RequestBody PostCateCreate postCateCreate) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/cate";
        return restTemplate.postForObject(url, postCateCreate, Result.class);
    }

    @PutMapping("/docs/cate/{cateId}")
    public Result updateDocsCate(@PathVariable Long cateId, @RequestBody PostCateUpdate postCateUpdate) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/cate/{cateId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<PostCateUpdate> entity = new HttpEntity<>(postCateUpdate, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, Result.class, cateId).getBody();
    }

    @DeleteMapping("/docs/cate/{cateId}")
    public Result deleteDocsCate(@PathVariable Long cateId) {
        String url = DocsConsts.DOCS_SERVER_HOST + "/docs/cate/{cateId}";
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Result.class, cateId).getBody();
    }
}
