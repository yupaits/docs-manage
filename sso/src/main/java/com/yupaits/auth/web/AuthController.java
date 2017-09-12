package com.yupaits.auth.web;

import com.yupaits.auth.bean.LoginForm;
import com.yupaits.auth.bean.RegisterForm;
import com.yupaits.auth.config.jwt.JwtHelper;
import com.yupaits.auth.config.jwt.JwtProperties;
import com.yupaits.auth.config.jwt.TokenUserInfo;
import com.yupaits.docs.mapper.RoleMapper;
import com.yupaits.docs.mapper.UserMapper;
import com.yupaits.docs.mapper.UserRoleMapper;
import com.yupaits.docs.model.Role;
import com.yupaits.docs.model.User;
import com.yupaits.docs.model.UserRole;
import com.yupaits.auth.util.encrypt.EncryptUtils;
import com.yupaits.docs.bean.UserDTO;
import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
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
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        if (loginForm == null || StringUtils.isBlank(loginForm.getUsername()) || StringUtils.isBlank(loginForm.getPassword())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        User user = userMapper.selectByUsername(loginForm.getUsername());
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
        userMapper.insertSelective(user);
        User userInDb = userMapper.selectByUsername(user.getUsername());
        if (userInDb == null) {
            return Result.fail(ResultCode.DB_ERROR);
        }
        Role role = roleMapper.selectByRoleName("user");
        Assert.notNull(role, "role[user] is null!");
        UserRole userRole = new UserRole(userInDb.getId(), role.getId());
        userRoleMapper.insert(userRole);
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
            tokenUserInfo = new TokenUserInfo(refreshedToken, userDTO, jwtProperties.getExpiredIn() );
        }
        return Result.ok(tokenUserInfo);
    }
}
