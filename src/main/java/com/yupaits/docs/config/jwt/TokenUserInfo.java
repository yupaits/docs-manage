package com.yupaits.docs.config.jwt;

import com.yupaits.docs.bean.UserDTO;
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
    private Long expiredAt;
}
