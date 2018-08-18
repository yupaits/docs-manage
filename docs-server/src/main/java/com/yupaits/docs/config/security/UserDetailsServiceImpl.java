package com.yupaits.docs.config.security;

import com.yupaits.docs.entity.User;
import com.yupaits.docs.entity.UserRole;
import com.yupaits.docs.repository.RoleRepository;
import com.yupaits.docs.repository.UserRepository;
import com.yupaits.docs.repository.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                                  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' doesn't exists!", username));
        } else {
            user.setRoles(roleRepository.findAllByIdIsIn(userRoleRepository.findAllByUserId(user.getId()).stream()
                    .map(UserRole::getRoleId)
                    .collect(Collectors.toList())));
        }
        return user;
    }
}
