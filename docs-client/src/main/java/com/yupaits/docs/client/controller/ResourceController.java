package com.yupaits.docs.client.controller;

import com.yupaits.docs.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yupaits
 * @date 2018/8/21
 */
@RestController
public class ResourceController {

    private final OAuth2RestTemplate restTemplate;

    @Autowired
    public ResourceController(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/docs**")
    public Result getDocsResource(HttpServletRequest request) {
        Result result = restTemplate.getForObject(request.getPathInfo(), Result.class);
        return result;
    }
}
