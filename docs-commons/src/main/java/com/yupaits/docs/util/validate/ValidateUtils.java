package com.yupaits.docs.util.validate;

/**
 * 验证工具类
 * Created by ts495 on 2017/9/9.
 */
public class ValidateUtils {

    /**
     * ID为空
     * @param id ID
     * @return 验证结果
     */
    public static boolean idInvalid(Integer id) {
        return id == null || id.compareTo(0) <= 0;
    }

    public static boolean isUnsignedInteger(Integer integer) {
        return integer != null && integer.compareTo(0) >= 0;
    }
}
