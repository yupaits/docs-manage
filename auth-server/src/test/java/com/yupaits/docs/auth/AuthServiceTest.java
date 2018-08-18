package com.yupaits.docs.auth;

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
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yupaits
 * @date 2018/8/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    public void test01AddRoles() {
        roleRepository.save(new Role("admin"));
        roleRepository.save(new Role("user"));
    }

    @Test
    public void test02RegisterUsers() {
        authService.register(new RegisterForm("admin", "qq@qq.com", "123456", "123456"));
        authService.register(new RegisterForm("user", "163@163.com", "123456", "123456"));
    }

    @Test
    public void test03GetUser() {
        Result result = authService.getUserByUsername("admin");
        System.out.println(result);
        Assert.assertTrue(result.getCode() == ResultCode.OK.getCode() && result.getData() != null
                && ((UserVO) result.getData()).getRoles().size() > 0);
    }

    @Test
    public void test04UserAddRole() {
        User user = userRepository.findByUsername("admin");
        Role role = roleRepository.findByRoleName("admin");
        UserRole userRole = new UserRole(user.getId(), role.getId());
        userRoleRepository.save(userRole);
    }

    @Test
    public void test05DeleteUser() {
        authService.register(new RegisterForm("test", "test@test.com", "test", "test"));
        User user = userRepository.findByUsername("test");
        userRoleRepository.delete(userRoleRepository.findAllByUserId(user.getId()));
        userRepository.delete(user.getId());
    }
}
