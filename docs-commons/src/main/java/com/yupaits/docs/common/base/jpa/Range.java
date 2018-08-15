package com.yupaits.docs.common.base.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询范围
 * @author yupaits
 * @date 2018/7/6
 */
@Data
@AllArgsConstructor
public class Range<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String field;
    private Comparable from;
    private Comparable to;
    private Boolean includeNull;

    public Range(String field) {
        this.field = field;
    }

    public Range(String field, Comparable from, Comparable to) {
        this.field = field;
        this.from = from;
        this.to = to;
    }

    public Range(Range<E> other) {
        this.field = other.getField();
        this.from = other.getFrom();
        this.to = other.getTo();
        this.includeNull = other.getIncludeNull();
    }

    public boolean isFromSet() {
        return getFrom() != null;
    }

    public boolean isToSet() {
        return getTo() != null;
    }

    public boolean isIncludeNullSet() {
        return getIncludeNull() != null;
    }

    public boolean isBetween() {
        return isFromSet() && isToSet();
    }

    public boolean isSet() {
        return isFromSet() || isToSet() || isIncludeNullSet();
    }

    public boolean isValid() {
        return !isBetween() || getFrom().compareTo(getTo()) <= 0;
    }
}
