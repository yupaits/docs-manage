package com.yupaits.docs.common.constants;

/**
 * @author yupaits
 * @date 2018/8/11
 */
public class DocsConsts {
    public static final String DEV_USERNAME = "dev";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_NAME = "AccessToken";
    public static final String VALID_TOKEN_STORE = "valid_token";
    public static final String REFRESH_TTL_KEY = "refresh_ttl:";
    public static final String POST_CATES_LIST = "post_cates";
    public static final String POST_TAG_SET = "post_tags";

    public static String[] ignorePaths = new String[]{"/doc.html", "/v2/api-docs", "/swagger-resources/**",
            "/webjars/**", "/login", "register"};
}
