package com.yupaits.docs.entity;

import com.yupaits.docs.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "docs_user")
public class User extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "salt", length = 36)
    private String salt;

    @Column(name = "password", length = 64)
    private String password;

    @Transient
    private List<Role> roles = new ArrayList<>();

    public String getCredential() {
        return username + salt;
    }
}
