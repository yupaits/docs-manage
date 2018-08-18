package com.yupaits.docs.common.base.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.ExampleMatcher;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/7/6
 */
@Data
@AllArgsConstructor
public class ExampleProperty<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段匹配方式
     */
    private ExampleMatcher.StringMatcher matcher;

    /**
     * 字段名称
     */
    private final String name;

    /**
     * 匹配值
     */
    private final T value;
}
