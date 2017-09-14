package com.yupaits.docs.repository;

import com.yupaits.docs.entity.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DirectoryRepository
 * Created by yupaits on 2017/9/13.
 */
public interface DirectoryRepository extends JpaRepository<Directory, Integer> {

    List<Directory> findByProjectIdAndParentIdOrderBySortCodeAsc(Integer projectId, Integer parentId);

    @Override
    void delete(Integer directoryId);

    @Override
    Directory findOne(Integer directoryId);

    @Override
    <S extends Directory> S save(S s);

    List<Directory> findByParentIdOrderBySortCodeAsc(Integer parentId);
}
