package com.yupaits.docs.security.service;

import com.yupaits.docs.common.constant.ApplicationConstant;
import com.yupaits.docs.mapper.RoleMapper;
import com.yupaits.docs.mapper.UserMapper;
import com.yupaits.docs.model.Role;
import com.yupaits.docs.security.model.User;
import com.yupaits.docs.utils.redis.RedisSupport;
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
    private RedisSupport redisSupport;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) redisSupport.get(ApplicationConstant.USER_INFO_PREFIX + username);
        if (user == null) {
            user = userMapper.selectByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found!", username));
        } else {
            redisSupport.put(ApplicationConstant.USER_INFO_PREFIX + username, user);
            @SuppressWarnings("unchecked") Set<GrantedAuthority> authorities =
                    (Set<GrantedAuthority>) redisSupport.get(ApplicationConstant.USER_AUTHORITIES_PREFIX + username);
            if (authorities == null) {
                List<Role> roles = roleMapper.selectByUsername(username);
                if (CollectionUtils.isNotEmpty(roles)) {
                    authorities = new HashSet<>();
                    SimpleGrantedAuthority authority = null;
                    for (Role role : roles) {
                        authority = new SimpleGrantedAuthority(role.getRoleName());
                        authorities.add(authority);
                    }
                    redisSupport.put(ApplicationConstant.USER_AUTHORITIES_PREFIX + username, authorities);
                }
            }
            user.setAuthorities(authorities);
        }
        return user;
    }
}
