package com.yupaits.docs.entity;

import com.yupaits.docs.entity.keys.UserRoleKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserRoleKey.class)
@Table(name = "docs_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
    private Long roleId;
}
