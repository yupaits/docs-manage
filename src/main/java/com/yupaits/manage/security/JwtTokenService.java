package com.yupaits.manage.security;


import org.springframework.security.core.Authentication;

/**
 * Created by yupaits on 2017/8/5.
 */
public interface JwtTokenService {

    String createJwtToken(Authentication auth, int minutes);

    Authentication parseJwtToken(String jwtToken);
}
