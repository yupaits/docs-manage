package com.yupaits.docs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册表单封装
 * @author ts495
 * @date 2017/9/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
