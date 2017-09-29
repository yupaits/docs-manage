package com.yupaits.docs.config;

import com.yupaits.auth.entity.User;
import com.yupaits.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Jwt辅助类
 * Created by yupaits on 2017/8/7.
 */
@Component
public class JwtHelper {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserRepository userRepository;

    /**
     * 从Request中获取token
     * @param request 请求体
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        Cookie authCookie = getCookieValue(request, jwtProperties.getAuthCookie());
        if (authCookie != null) {
            return authCookie.getValue();
        }
        String authHeader = request.getHeader(jwtProperties.getAuthHeader());
        if (authHeader != null && authHeader.startsWith("bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 从token中获取username信息
     * @param token token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从Request重获取userId信息
     * @param request 请求体
     * @return userId
     */
    public Integer getUserId(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String username = getUsernameFromToken(token);
        if (StringUtils.isBlank(username)) {
            return null;
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    /**
     * 获取声明的内容
     * @param token jwt
     * @return 已声明的内容
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 获取指定名称的Cookie
     * @param request 请求
     * @param name Cookie名
     * @return 指定Cookie
     */
    public Cookie getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (StringUtils.equals(cookie.getName(), name)) {
                return cookie;
            }
        }
        return null;
    }
}
