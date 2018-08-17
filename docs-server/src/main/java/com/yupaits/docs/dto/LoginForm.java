package com.yupaits.docs.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录表单封装
 * @author ts495
 * @date 2017/9/9
 */
@Data
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
