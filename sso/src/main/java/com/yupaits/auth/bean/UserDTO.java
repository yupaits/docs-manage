package com.yupaits.auth.bean;

import lombok.Data;

/**
 * 用户DTO类
 * Created by yupaits on 2017/8/10.
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
}
