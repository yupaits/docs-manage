package com.yupaits.docs.auth.controller;

import com.yupaits.docs.auth.dto.RegisterForm;
import com.yupaits.docs.auth.entity.Role;
import com.yupaits.docs.auth.entity.User;
import com.yupaits.docs.auth.service.AuthService;
import com.yupaits.docs.auth.vo.UserVO;
import com.yupaits.docs.common.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collectors;

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

    /*@GetMapping("/user")
    public Principal getCurrentUser(Principal user) {
        return user;
    }*/

    @GetMapping("/user")
    public Result getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setId(user.getId());
        userVO.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        return Result.ok(userVO);
    }

    @GetMapping("/oauth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new SecurityContextLogoutHandler().logout(request, null, null);
        response.sendRedirect(request.getHeader("referer"));
    }
}
