package com.yupaits.docs.config.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AnonymousAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    public AnonymousAuthentication() {
        super(null);
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
