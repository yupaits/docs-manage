package com.yupaits.docs.controller;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.EncryptUtils;
import com.yupaits.docs.common.utils.SecurityContextUtils;
import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.config.JwtProperties;
import com.yupaits.docs.dto.LoginForm;
import com.yupaits.docs.dto.RegisterForm;
import com.yupaits.docs.entity.Role;
import com.yupaits.docs.entity.User;
import com.yupaits.docs.repository.RoleRepository;
import com.yupaits.docs.repository.UserRepository;
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
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        if (loginForm == null || StringUtils.isBlank(loginForm.getUsername()) || StringUtils.isBlank(loginForm.getPassword())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        User user = userRepository.findByUsername(loginForm.getUsername());
        if (user == null || !StringUtils.equals(EncryptUtils.encryptPassword(loginForm.getPassword(), user.getCredential()), user.getPassword())) {
            return Result.fail(ResultCode.LOGIN_FAIL);
        }
        return Result.ok(jwtHelper.generateToken(loginForm.getUsername()));
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm) {
        if (registerForm == null || StringUtils.isBlank(registerForm.getUsername()) || StringUtils.isBlank(registerForm.getEmail())
                || StringUtils.isBlank(registerForm.getPassword()) || StringUtils.isBlank(registerForm.getConfirmPassword())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            return Result.fail("两次输入的密码不匹配");
        }
        User user = new User();
        BeanUtils.copyProperties(registerForm, user);
        String salt = UUID.nameUUIDFromBytes(user.getUsername().getBytes()).toString();
        user.setSalt(salt);
        user.setPassword(EncryptUtils.encryptPassword(user.getPassword(), user.getCredential()));
        Role role = roleRepository.findByRoleName("user");
        Assert.notNull(role, "role[user] is null!");
        user.getRoles().add(role);
        userRepository.save(user);
        return Result.ok();
    }

    @GetMapping("/user")
    public Result getCurrentUser(HttpServletRequest request) {
        UserVO userVO = null;
        String authToken = jwtHelper.getToken(request);
        if (authToken != null) {
            User user = userRepository.findByUsername(SecurityContextUtils.currentUsername());
            if (user != null) {
                userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);

            }
        }
        return Result.ok(userVO);
    }

    @GetMapping("/refresh")
    public Result refreshAuthToken(HttpServletRequest request) {
        String authToken = jwtHelper.getToken(request);
        if (authToken == null || !jwtHelper.canRefreshToken(authToken)) {
            return Result.fail(ResultCode.TOKEN_REFRESH_INVALID);
        }
        //刷新token
        return Result.ok(jwtHelper.refreshToken(authToken));
    }
}
