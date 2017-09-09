package com.yupaits.docs.rest;

import com.yupaits.docs.bean.LoginForm;
import com.yupaits.docs.bean.UserDTO;
import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.config.jwt.JwtHelper;
import com.yupaits.docs.config.jwt.JwtProperties;
import com.yupaits.docs.config.jwt.TokenUserInfo;
import com.yupaits.docs.mapper.UserMapper;
import com.yupaits.docs.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        if (loginForm == null || StringUtils.isBlank(loginForm.getUsername()) || StringUtils.isBlank(loginForm.getPassword())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        User user = userMapper.selectByUsername(loginForm.getUsername());
        if (user == null || !StringUtils.equals(loginForm.getPassword(), user.getPassword())) {
            return Result.fail(ResultCode.LOGIN_FAIL);
        }
        String accessToken = jwtHelper.generateToken(loginForm.getUsername());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        TokenUserInfo tokenUserInfo = new TokenUserInfo(accessToken, userDTO, jwtHelper.generateExpirationTimeMillis(jwtProperties.getExpiredIn()));
        return Result.ok(tokenUserInfo);
    }

    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }

    @GetMapping("/currentUser")
    public Result getCurrentUser(HttpServletRequest request) {
        UserDTO currentUser = null;
        String authToken = jwtHelper.getToken(request);
        if (authToken != null) {
            User user = userMapper.selectByUsername(jwtHelper.getUsernameFromToken(authToken));
            if (user != null) {
                currentUser = new UserDTO();
                BeanUtils.copyProperties(user, currentUser);
            }
        }
        return Result.ok(currentUser);
    }

    @GetMapping("/refresh")
    public Result refreshAuthToken(HttpServletRequest request) {
        String authToken = jwtHelper.getToken(request);
        TokenUserInfo tokenUserInfo = null;
        if (authToken != null && jwtHelper.canRefreshToken(authToken)) {
            //刷新token
            String refreshedToken = jwtHelper.refreshToken(authToken);
            User user = userMapper.selectByUsername(jwtHelper.getUsernameFromToken(authToken));
            UserDTO userDTO = null;
            if (user != null) {
                userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
            }
            tokenUserInfo = new TokenUserInfo(refreshedToken, userDTO, jwtHelper.generateExpirationTimeMillis(jwtProperties.getExpiredIn()));
        }
        return Result.ok(tokenUserInfo);
    }
}
