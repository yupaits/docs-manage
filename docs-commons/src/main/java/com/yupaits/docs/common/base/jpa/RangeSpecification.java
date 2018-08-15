package com.yupaits.docs.common.base.jpa;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 范围查询规范
 * @author yupaits
 * @date 2018/7/6
 */
public class RangeSpecification<T> implements Specification<T> {
    private final List<Range<T>> ranges;

    public RangeSpecification(List<Range<T>> ranges) {
        this.ranges = ranges;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicateList = Lists.newArrayList();
        ranges.forEach(range -> {
            if (range.isSet()) {
                Predicate rangePredicate = buildRangePredicate(range, root, cb);
                if (rangePredicate != null) {
                    if (!range.isIncludeNullSet() || Boolean.FALSE.equals(range.getIncludeNull())) {
                        predicateList.add(rangePredicate);
                    } else {
                        predicateList.add(cb.or(rangePredicate, cb.isNull(root.get(range.getField()))));
                    }
                }

                if (Boolean.TRUE.equals(range.getIncludeNull())) {
                    predicateList.add(cb.isNull(root.get(range.getField())));
                } else if (Boolean.FALSE.equals(range.getIncludeNull())) {
                    predicateList.add(cb.isNotNull(root.get(range.getField())));
                }
            }
        });
        return CollectionUtils.isEmpty(predicateList) ? cb.conjunction() : cb.and(Iterables.toArray(predicateList, Predicate.class));
    }

    @SuppressWarnings("unchecked")
    private Predicate buildRangePredicate(Range<T> range, Root<T> root, CriteriaBuilder cb) {
        if (range.isBetween()) {
            return cb.between(root.get(range.getField()), range.getFrom(), range.getTo());
        } else if (range.isFromSet()) {
            return cb.greaterThanOrEqualTo(root.get(range.getField()), range.getFrom());
        } else if (range.isToSet()) {
            return cb.lessThanOrEqualTo(root.get(range.getField()), range.getTo());
        }
        return null;
    }
}
