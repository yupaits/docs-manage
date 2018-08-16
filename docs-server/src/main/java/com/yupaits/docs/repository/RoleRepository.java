package com.yupaits.docs.repository;

import com.yupaits.docs.common.base.BaseRepository;
import com.yupaits.docs.entity.Role;
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
