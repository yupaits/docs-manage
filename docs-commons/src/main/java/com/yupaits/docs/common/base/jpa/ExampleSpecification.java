package com.yupaits.docs.common.base.jpa;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 样板查询规范
 * @author yupaits
 * @date 2018/7/6
 */
public class ExampleSpecification<T> implements Specification<T> {
    private final ExampleNode<T> exampleNode;

    public ExampleSpecification(ExampleNode<T> exampleNode) {
        Assert.assertNotNull("ExampleNode must not empty.", exampleNode);
        this.exampleNode = exampleNode;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> rootPredicates = Lists.newArrayList();
        // 递归转换ExampleNode为Predicate
        return transformExampleNode(rootPredicates, exampleNode, root, cb);
    }

    private Predicate transformExampleNode(List<Predicate> predicates, ExampleNode<T> exampleNode, Root<T> root, CriteriaBuilder cb) {
        exampleNode.getPropertyList().forEach(exampleProperty -> addPropertyPredicate(predicates, root, cb, exampleProperty));
        exampleNode.getSubNodes().forEach(subExampleNode -> {
            List<Predicate> subPredicates = Lists.newArrayList();
            predicates.add(transformExampleNode(subPredicates, subExampleNode, root, cb));
        });
        return getPredicate(cb, predicates, exampleNode.getMatchMode());
    }

    private <P> void addPropertyPredicate(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, ExampleProperty<P> exampleProperty) {
        switch (exampleProperty.getMatcher()) {
            case DEFAULT:
            case EXACT:
                predicates.add(cb.equal(root.get(exampleProperty.getName()), exampleProperty.getValue()));
                break;
            case CONTAINING:
                predicates.add(cb.like(root.get(exampleProperty.getName()), "%" + exampleProperty.getValue() + "%"));
                break;
            case STARTING:
                predicates.add(cb.like(root.get(exampleProperty.getName()), exampleProperty.getValue() + "%"));
                break;
            case ENDING:
                predicates.add(cb.like(root.get(exampleProperty.getName()), "%" + exampleProperty.getValue()));
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported StringMatcher " + exampleProperty.getMatcher().name());
        }
    }

    /**
     * 根据匹配模式连接查询语句
     * @param cb CriteriaBuilder
     * @param predicates 待连接的若干查询语句
     * @param matchMode 匹配模式
     * @return 连接之后的查询语句
     */
    private Predicate getPredicate(CriteriaBuilder cb, List<Predicate> predicates, ExampleMatchMode matchMode) {
        if (matchMode == null || matchMode.equals(ExampleMatchMode.AND)) {
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        }
    }
}
