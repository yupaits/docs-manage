package com.yupaits.docs.util.bean;

import org.springframework.beans.BeanUtils;

/**
 * Bean工具类
 * Created by yupaits on 2017/9/13.
 */
public class BeanUtil {
    private static String[] ignoreProperties = {"id", "ownerId", "isDeleted", "create_at", "update_at"};

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
}
