package com.yupaits.docs.auth.service.impl;

import com.yupaits.docs.auth.dto.RegisterForm;
import com.yupaits.docs.auth.entity.Role;
import com.yupaits.docs.auth.entity.User;
import com.yupaits.docs.auth.entity.UserRole;
import com.yupaits.docs.auth.repository.RoleRepository;
import com.yupaits.docs.auth.repository.UserRepository;
import com.yupaits.docs.auth.repository.UserRoleRepository;
import com.yupaits.docs.auth.service.AuthService;
import com.yupaits.docs.auth.vo.UserVO;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yupaits
 * @date 2018/8/11
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Result register(RegisterForm registerForm) {
        if (!registerForm.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            return Result.fail("两次输入的密码不匹配");
        }
        User user = new User();
        BeanUtils.copyProperties(registerForm, user);
        String salt = UUID.nameUUIDFromBytes(user.getUsername().getBytes()).toString();
        user.setSalt(salt);
        user.setPassword(EncryptUtils.encryptPassword(user.getPassword(), user.getCredential()));
        Role role = roleRepository.findByRoleName("user");
        Assert.notNull(role, "role[user] is null!");
        User flushedUser = userRepository.save(user);
        UserRole userRole = new UserRole(flushedUser.getId(), role.getId());
        userRoleRepository.save(userRole);
        return Result.ok();
    }

    @Override
    public Result getCurrentUser() {
        UserVO userVO = null;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isNotBlank(username)) {
            userVO = getUserByName(username);
        }
        return Result.ok(userVO);
    }

    @Override
    public Result getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(getUserByName(username));
    }

    /**
     * 根据用户名查找用户信息
     * @param username 用户名
     * @return UserVO
     */
    private UserVO getUserByName(String username) {
        UserVO userVO = null;
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setId(user.getId());
            userVO.setRoles(roleRepository.findAllByIdIsIn(userRoleRepository.findAllByUserId(user.getId())
                    .stream().map(UserRole::getRoleId).collect(Collectors.toList()))
                    .stream().map(Role::getRoleName).collect(Collectors.toList()));
        }
        return userVO;
    }
}
