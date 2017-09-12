package com.yupaits.docs.common;

import org.apache.shiro.codec.Base64;

/**
 * 常量定义
 * Created by yupaits on 2017/8/7.
 */
public class AppConstant {
    public static final String ENCRYPT_EXTRA_SALT = new String(Base64.decode("eXVwYWl0cw==".getBytes()));

}
