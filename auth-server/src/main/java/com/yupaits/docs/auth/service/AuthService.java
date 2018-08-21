package com.yupaits.docs.auth.service;

import com.yupaits.docs.auth.dto.RegisterForm;
import com.yupaits.docs.auth.vo.UserVO;
import com.yupaits.docs.common.result.Result;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public interface AuthService {

    Result register(RegisterForm registerForm);

    Result getUserByUsername(String username);
}
