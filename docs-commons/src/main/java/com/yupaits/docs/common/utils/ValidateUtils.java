package com.yupaits.docs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public class ValidateUtils {

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(Long id) {
        return id != null && id.compareTo(0L) > 0;
    }

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(Integer id) {
        return id != null && id.compareTo(0) > 0;
    }

    /**
     * 判断是否为正整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isPositive(Integer number) {
        return number != null && number.compareTo(0) > 0;
    }

    /**
     * 判断是否为正小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isPositive(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断是否为负整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNegative(Integer number) {
        return number != null && number.compareTo(0) < 0;
    }

    /**
     * 判断是否为负小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNegative(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 判断是否为非负整数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNotNegative(Integer number) {
        return number != null && number.compareTo(0) >= 0;
    }

    /**
     * 判断是否为非负小数
     * @param number 待判断的数
     * @return 校验结果
     */
    public static boolean isNotNegative(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 检查ID是否有效
     * @param id id
     * @return 校验结果
     */
    public static boolean idValid(String id) {
        return StringUtils.isNotBlank(id);
    }

    /**
     * 检查手机号码是否有效
     * @param phoneNumber 手机号码
     * @return 校验结果
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        String regex = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        return Pattern.compile(regex).matcher(phoneNumber).matches();
    }

}
