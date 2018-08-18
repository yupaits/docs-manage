package com.yupaits.docs.config.security;

import com.yupaits.docs.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TokenAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    private String token;
    private final User principal;

    public TokenAuthentication(User principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public String getName() {
        return principal.getUsername();
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
        return principal;
    }
}
