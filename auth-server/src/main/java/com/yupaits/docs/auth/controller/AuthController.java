package com.yupaits.docs.auth.controller;

import com.yupaits.docs.auth.dto.RegisterForm;
import com.yupaits.docs.auth.service.AuthService;
import com.yupaits.docs.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权开放接口
 * @author yupaits
 * @date 2017/8/8
 */
@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm) {
        return authService.register(registerForm);
    }

    @GetMapping("/user")
    public Result getCurrentUser() {
        return authService.getCurrentUser();
    }
}
