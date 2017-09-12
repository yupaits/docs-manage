package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String email;
    private String salt;
    private String password;
    private List<Role> roles;

    public String getCredential() {
        return username + salt;
    }
}
