package com.yupaits.auth.config.jwt;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

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

    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

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
     * 生成token
     * @param username username
     * @return token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(applicationName)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, jwtProperties.getSecret())
                .compact();
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 能否刷新token
     * @param token token
     * @return 判定结果
     */
    public Boolean canRefreshToken(String token) {
        try {
            final Date expirationDate = getClaimsFromToken(token).getExpiration();
            return expirationDate.compareTo(new Date()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.setIssuedAt(new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 获取过期时间戳
     * @param expiredIn 预设的过期时间，单位：分钟
     * @return 预期的过期时间戳
     */
    public Long generateExpirationTimeMillis(int expiredIn) {
        return System.currentTimeMillis() + expiredIn * 1000;
    }

    /**
     * 得到过期日期时间
     * @return 预期的过期日期时间信息
     */
    private Date generateExpirationDate() {
        return new Date(generateExpirationTimeMillis(jwtProperties.getExpiredIn()));
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
