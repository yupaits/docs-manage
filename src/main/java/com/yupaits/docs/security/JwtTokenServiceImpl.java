package com.yupaits.docs.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

/**
 * Created by yupaits on 2017/8/5.
 */
public class JwtTokenServiceImpl implements JwtTokenService {
    private final Key secret = MacProvider.generateKey();

    @Override
    public String createJwtToken(Authentication authentication, int minutes) {
        Claims claims = Jwts.claims()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .setIssuedAt(new Date());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public Authentication parseJwtToken(String jwtToken) {
        return null;
    }
}
