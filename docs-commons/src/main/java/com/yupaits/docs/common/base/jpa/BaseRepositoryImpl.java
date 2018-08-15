package com.yupaits.docs.common.base.jpa;

import com.yupaits.docs.common.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * 字段、取值范围查询实现
 * @author yupaits
 * @date 2018/7/6
 */
public class BaseRepositoryImpl<E, PK extends Serializable> extends SimpleJpaRepository<E, PK> implements BaseRepository<E, PK> {
    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<E, PK> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Page<E> findByExampleWithRange(ExampleNode<E> exampleNode, List<Range<E>> ranges, Pageable pageable) {
        Specification<E> exampleSpecification = new ExampleSpecification<>(exampleNode);
        Specification<E> rangesSpecification = new RangeSpecification<>(ranges);
        return findAll(Specifications.where(exampleSpecification).and(rangesSpecification), pageable);
    }

    @Override
    public Page<E> findByExample(ExampleNode<E> exampleNode, Pageable pageable) {
        Specification<E> exampleSpecification = new ExampleSpecification<>(exampleNode);
        return findAll(exampleSpecification, pageable);
    }
}
