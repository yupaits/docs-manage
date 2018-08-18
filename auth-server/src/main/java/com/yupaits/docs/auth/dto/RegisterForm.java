package com.yupaits.docs.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    @ApiModelProperty(hidden = true)
    public boolean isValid() {
        return !StringUtils.isAnyBlank(username, email, password, confirmPassword);
    }
}
