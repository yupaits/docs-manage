package com.yupaits.docs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
@AllArgsConstructor
public class TokenRefresh implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private Date refreshDeadline;
}
