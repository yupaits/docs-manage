package com.yupaits.docs.common.base;

import com.yupaits.docs.common.base.jpa.ExampleNode;
import com.yupaits.docs.common.base.jpa.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * 基础仓库类
 * @author yupaits
 * @date 2018/7/6
 */
@NoRepositoryBean
public interface BaseRepository<E, PK extends Serializable> extends JpaRepository<E, PK> {

    /**
     * 根据字段动态条件和范围查询
     * @param exampleNode 动态条件查询对象
     * @param ranges 范围查询参数
     * @param pageable 分页、排序参数
     * @return 查询结果
     */
    Page<E> findByExampleWithRange(ExampleNode<E> exampleNode, List<Range<E>> ranges, Pageable pageable);

    /**
     * 根据字段动态条件查询
     * @param exampleNode 动态条件查询对象
     * @param pageable 分页、排序参数
     * @return 查询结果
     */
    Page<E> findByExample(ExampleNode<E> exampleNode, Pageable pageable);
}

