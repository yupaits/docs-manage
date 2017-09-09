package com.yupaits.docs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ts495 on 2017/9/9.
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping("/register.html")
    public String register() {
        return "register";
    }

    @GetMapping("/forgetPassword.html")
    public String forgetPassword() {
        return "forgetPassword";
    }

    @GetMapping("/passwordReset.html")
    public String passwordReset() {
        return "passwordReset";
    }

    @GetMapping("/feedback.html")
    public String feedback() {
        return "feedback";
    }
}
