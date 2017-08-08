package com.yupaits.docs.security.helper;

import com.alibaba.druid.util.StringUtils;
import com.yupaits.docs.security.service.DefaultUserDetailsService;
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
 * Created by yupaits on 2017/8/7.
 */
@Component
public class TokenHelper {
    @Value("${app.name}")
    private String appName;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiredIn}")
    private int expiredIn;

    @Value("${jwt.header}")
    private String authHeaderName;

    @Value("${jwt.cookie}")
    private String authCookieName;

    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private final String authorities = "authorities";

    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;

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

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

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

    public Long generateExpirationTimeMillis(int expiredIn) {
        return System.currentTimeMillis() + expiredIn * 1000;
    }

    private Date generateExpirationDate() {
        return new Date(generateExpirationTimeMillis(expiredIn));
    }

    public String getToken(HttpServletRequest request) {
        Cookie authCookie = getCookieValue(request, authCookieName);
        if (authCookie != null) {
            return authCookie.getValue();
        }
        String authHeader = request.getHeader(authHeaderName);
        if (authHeader != null && authHeader.startsWith("bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

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
