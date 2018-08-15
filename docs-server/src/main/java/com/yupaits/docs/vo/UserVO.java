package com.yupaits.docs.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/11
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
}
