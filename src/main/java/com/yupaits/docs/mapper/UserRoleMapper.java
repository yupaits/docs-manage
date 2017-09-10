package com.yupaits.docs.mapper;

import com.yupaits.docs.model.UserRole;

import java.util.List;

public interface UserRoleMapper {

    int delete(UserRole userRole);

    int deleteByUserId(Integer userId);

    int deleteByRoleId(Integer roleId);

    int insert(UserRole userRole);

    List<UserRole> selectByUserId(Integer userId);

    List<UserRole> selectByRoleId(Integer roleId);
}
