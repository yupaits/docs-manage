package com.yupaits.docs.security.auth;

import com.yupaits.docs.security.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by yupaits on 2017/8/7.
 */
public class TokenBasedAuthentication extends AbstractAuthenticationToken {
    private String token;
    private final User principle;

    public TokenBasedAuthentication(User principle) {
        super(principle.getAuthorities());
        this.principle = principle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principle;
    }
}
