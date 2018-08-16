package com.yupaits.docs.repository;

import com.yupaits.docs.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/12
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findAllByUserId(Long userId);

    List<UserRole> findAllByRoleId(Long roleId);
}
