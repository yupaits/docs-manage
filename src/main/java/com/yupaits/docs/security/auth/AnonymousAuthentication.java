package com.yupaits.docs.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by yupaits on 2017/8/7.
 */
public class AnonymousAuthentication extends AbstractAuthenticationToken {
    public AnonymousAuthentication() {
        super(null);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
