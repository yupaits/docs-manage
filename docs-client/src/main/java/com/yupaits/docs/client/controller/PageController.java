package com.yupaits.docs.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yupaits
 * @date 2018/8/20
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/secured")
    public String securedPage() {
        return "securedPage";
    }
}
