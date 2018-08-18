package com.yupaits.docs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录表单封装
 * @author ts495
 * @date 2017/9/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
