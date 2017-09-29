package com.yupaits.auth.web;

import com.yupaits.auth.bean.LoginForm;
import com.yupaits.auth.bean.RegisterForm;
import com.yupaits.auth.bean.UserDTO;
import com.yupaits.auth.config.JwtHelper;
import com.yupaits.auth.config.JwtProperties;
import com.yupaits.auth.bean.TokenUserInfo;
import com.yupaits.auth.entity.Role;
import com.yupaits.auth.entity.User;
import com.yupaits.auth.repository.RoleRepository;
import com.yupaits.auth.repository.UserRepository;
import com.yupaits.docs.response.Result;
import com.yupaits.docs.response.ResultCode;
import com.yupaits.docs.util.encrypt.EncryptUtils;
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
        String accessToken = jwtHelper.generateToken(loginForm.getUsername());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        TokenUserInfo tokenUserInfo = new TokenUserInfo(accessToken, userDTO, jwtProperties.getExpiredIn());
        return Result.ok(tokenUserInfo);
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

    @GetMapping("/currentUser")
    public Result getCurrentUser(HttpServletRequest request) {
        UserDTO currentUser = null;
        String authToken = jwtHelper.getToken(request);
        if (authToken != null) {
            User user = userRepository.findByUsername(jwtHelper.getUsernameFromToken(authToken));
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
            User user = userRepository.findByUsername(jwtHelper.getUsernameFromToken(authToken));
            UserDTO userDTO = null;
            if (user != null) {
                userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
            }
            tokenUserInfo = new TokenUserInfo(refreshedToken, userDTO, jwtProperties.getExpiredIn() );
        }
        return Result.ok(tokenUserInfo);
    }
}
