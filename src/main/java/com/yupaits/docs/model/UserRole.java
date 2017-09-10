package com.yupaits.docs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer roleId;
}
