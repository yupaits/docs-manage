package com.yupaits.docs.service;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.dto.LoginForm;
import com.yupaits.docs.dto.RegisterForm;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public interface AuthService {

    Result login(LoginForm loginForm);

    Result register(RegisterForm registerForm);

    Result getCurrentUser();

    Result getUserByUsername(String username);
}
