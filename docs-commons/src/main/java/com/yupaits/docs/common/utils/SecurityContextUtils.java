package com.yupaits.docs.common.utils;

import org.apache.shiro.SecurityUtils;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public class SecurityContextUtils {

    /**
     * 获取当前操作者用户名
     * @return 用户名
     */
    public static String currentUsername() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }
}
