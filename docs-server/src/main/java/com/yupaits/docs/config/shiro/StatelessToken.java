package com.yupaits.docs.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 无状态Token
 * @author ts495
 * @date 2017/9/9
 */
public class StatelessToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;

    private String username;
    private String token;

    public StatelessToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
