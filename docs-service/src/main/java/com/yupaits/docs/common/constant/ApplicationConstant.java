package com.yupaits.docs.common.constant;

import org.springframework.security.crypto.codec.Base64;

/**
 * Created by yupaits on 2017/8/7.
 */
public class ApplicationConstant {

    public static final String ENCRYPT_EXTRA_SALT = new String(Base64.decode("eXVwYWl0cw==".getBytes()));

    public static final String TOKEN_PAYLOAD_KEY = "payload";

    // Redis Cache key
    public static final String DOCS_DATA = "docs_data";

    public static final String USER_INFO_PREFIX = "user_info_";
    public static final String USER_AUTHORITIES_PREFIX = "user_authorities_";
}
