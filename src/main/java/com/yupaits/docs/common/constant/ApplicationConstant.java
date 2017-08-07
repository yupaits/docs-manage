package com.yupaits.docs.common.constant;

import org.springframework.security.crypto.codec.Base64;

/**
 * Created by yupaits on 2017/8/7.
 */
public class ApplicationConstant {

    public static String APPLICATION_JSON_VALUE = "application/json;charset=utf-8";
    public static final String ENCRYPT_EXTRA_SALT = new String(Base64.decode("eXVwYWl0cw==".getBytes()));
}
