package com.yupaits.docs.client.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yupaits
 * @date 2018/8/20
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String authorize(@RequestParam String code, @RequestParam String state) {
        return code;
    }

    @GetMapping("/secured")
    public String securedPage(Model model) {
        model.addAttribute("authentication", SecurityContextHolder.getContext().getAuthentication());
        return "securedPage";
    }
}
