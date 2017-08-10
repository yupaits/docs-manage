package com.yupaits.docs.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.security.helper.TokenHelper;
import com.yupaits.docs.security.model.User;
import com.yupaits.docs.security.model.UserDto;
import com.yupaits.docs.security.model.UserTokenState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yupaits on 2017/8/7.
 */
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Value("${jwt.expiredIn}")
    private int expiredIn;

    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Value("${app.userCookie}")
    private String userCookieName;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        User user = (User) authentication.getPrincipal();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        logger.debug("login success userDto: {}", userDto.toString());
        String jws = tokenHelper.generateToken(user.getUsername());
        //创建Token鉴权Cookie
//        Cookie authCookie = new Cookie(tokenCookie, jws);
//        authCookie.setPath("/");
//        authCookie.setHttpOnly(true);
//        authCookie.setMaxAge(expiredIn);
        //创建用户标识Cookie
//        Cookie userCookie = new Cookie(userCookieName, user.getUsername());
//        userCookie.setPath("/");
//        userCookie.setMaxAge(expiredIn);
        //添加Cookie到response
//        response.addCookie(authCookie);
//        response.addCookie(userCookie);
        UserTokenState userTokenState = new UserTokenState(jws, userDto, tokenHelper.generateExpirationTimeMillis(expiredIn));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(response.getWriter(), userTokenState);
    }
}
