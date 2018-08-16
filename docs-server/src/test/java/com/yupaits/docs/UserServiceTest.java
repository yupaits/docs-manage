package com.yupaits.docs;

import com.yupaits.docs.dto.RegisterForm;
import com.yupaits.docs.entity.Role;
import com.yupaits.docs.repository.RoleRepository;
import com.yupaits.docs.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yupaits
 * @date 2018/8/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testAddRoles() {
        roleRepository.save(new Role("admin"));
        roleRepository.save(new Role("user"));
    }

    @Test
    public void testRegisterUsers() {
        authService.register(new RegisterForm("admin", "qq@qq.com", "admin", "admin"));
        authService.register(new RegisterForm("user", "163@163.com", "user", "user"));
    }
}
