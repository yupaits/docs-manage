package com.yupaits.docs.entity.keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/12
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;
}
