package com.yupaits.docs.auth.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yupaits.docs.common.utils.serializer.LongJsonSerializer;
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

    @JsonSerialize(using = LongJsonSerializer.class)
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
