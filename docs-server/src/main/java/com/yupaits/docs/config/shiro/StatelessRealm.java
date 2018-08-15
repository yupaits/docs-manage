package com.yupaits.docs.config.shiro;

import com.yupaits.docs.entity.Role;
import com.yupaits.docs.entity.User;
import com.yupaits.docs.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 使用Jwt进行认证授权
 * Created by ts495 on 2017/9/9.
 */
public class StatelessRealm extends AuthorizingRealm {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        User user = userRepository.findByUsername(username);
        List<Role> roleList = user.getRoles();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = roleList.stream().map(Role::getRoleName).collect(Collectors.toSet());
        info.addRoles(roleSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        StatelessToken token = (StatelessToken) authenticationToken;
        Assert.notNull(authenticationToken, "token is null!");
        String username = (String) token.getPrincipal();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(String.format("user '%s' not found.", username));
        }
        String accessToken = (String) token.getCredentials();
        return new SimpleAuthenticationInfo(username, accessToken, getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }
}
