package com.yupaits.docs.security.service;

import com.yupaits.docs.mapper.RoleMapper;
import com.yupaits.docs.mapper.UserMapper;
import com.yupaits.docs.model.Role;
import com.yupaits.docs.security.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yupaits on 2017/8/7.
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User not found with username '%s'.", username));
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<Role> roles = roleMapper.selectByUsername(username);
        if (CollectionUtils.isNotEmpty(roles)) {
            SimpleGrantedAuthority authority = null;
            for (Role role : roles) {
                authority = new SimpleGrantedAuthority(role.getRoleName());
                authorities.add(authority);
            }
            user.setAuthorities(authorities);
        }
        return user;
    }
}
