package com.yupaits.auth.config.jwt;

import com.yupaits.auth.bean.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Token用户信息
 * Created by yupaits on 2017/8/7.
 */
@Data
@AllArgsConstructor
public class TokenUserInfo {
    private String accessToken;
    private UserDTO user;
    private Integer expiredIn;
}
