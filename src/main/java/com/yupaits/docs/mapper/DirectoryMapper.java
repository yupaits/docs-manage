package com.yupaits.docs.mapper;

import com.yupaits.docs.model.Directory;

import java.util.List;

public interface DirectoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Directory record);

    int insertSelective(Directory record);

    Directory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Directory record);

    int updateByPrimaryKey(Directory record);

    List<Directory> selectBySelective(Directory record);
}