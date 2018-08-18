package com.yupaits.docs.auth.repository;

import com.yupaits.docs.auth.entity.Role;
import com.yupaits.docs.common.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleRepository
 * Created by yupaits on 2017/9/13.
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Role findByRoleName(String roleName);

    List<Role> findAllByIdIsIn(List<Long> roleIds);
}
