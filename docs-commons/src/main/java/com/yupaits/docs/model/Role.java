package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private static final long serialVersionUID = -3304832283374325931L;

    private Integer id;
    private String roleName;
}