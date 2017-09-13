package com.yupaits.auth.repository;

import com.yupaits.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RoleRepository
 * Created by yupaits on 2017/9/13.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
