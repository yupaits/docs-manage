package com.yupaits.docs.controller;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.EncryptUtils;
import com.yupaits.docs.common.utils.SecurityContextUtils;
import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.dto.LoginForm;
import com.yupaits.docs.dto.RegisterForm;
import com.yupaits.docs.entity.Role;
import com.yupaits.docs.entity.User;
import com.yupaits.docs.repository.RoleRepository;
import com.yupaits.docs.repository.UserRepository;
import com.yupaits.docs.service.AuthService;
import com.yupaits.docs.vo.UserVO;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 授权开放接口
 * Created by yupaits on 2017/8/8.
 */
@RestController
@RequestMapping("/auth")
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

    @GetMapping("/refresh")
    public Result refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }
}
