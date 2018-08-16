package com.yupaits.docs.controller;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.dto.LoginForm;
import com.yupaits.docs.dto.RegisterForm;
import com.yupaits.docs.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权开放接口
 * Created by yupaits on 2017/8/8.
 */
@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        return authService.login(loginForm);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm) {
        return authService.register(registerForm);
    }

    @GetMapping("/user")
    public Result getCurrentUser(HttpServletRequest request) {
        return authService.getCurrentUser(request);
    }
}
