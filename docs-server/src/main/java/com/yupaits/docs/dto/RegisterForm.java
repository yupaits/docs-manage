package com.yupaits.docs.dto;

import lombok.Data;

/**
 * 注册表单封装
 * Created by ts495 on 2017/9/10.
 */
@Data
public class RegisterForm {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
