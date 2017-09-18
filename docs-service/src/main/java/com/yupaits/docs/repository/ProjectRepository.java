package com.yupaits.docs.repository;

import com.yupaits.docs.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProjectRepository
 * Created by yupaits on 2017/9/13.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByOwnerIdAndIsDeletedIsFalseOrderBySortCodeAsc(Integer ownerId);

    @Override
    <S extends Project> S save(S s);

    @Override
    Project findOne(Integer projectId);
}
