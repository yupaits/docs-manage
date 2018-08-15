package com.yupaits.docs.common.base.jpa;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Example条件查询结点
 * @author yupaits
 * @date 2018/7/6
 */
@Data
public class ExampleNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点匹配模式
     */
    private final ExampleMatchMode matchMode;

    /**
     * 当前节点待匹配的字段与匹配值集合
     */
    private List<ExampleProperty> propertyList = Lists.newArrayList();

    /**
     * 子查询节点集合
     */
    private List<ExampleNode<T>> subNodes = Lists.newArrayList();

    public ExampleNode(ExampleMatchMode matchMode) {
        this.matchMode = matchMode;
    }
}
