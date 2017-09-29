package com.yupaits.docs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt配置
 * Created by ts495 on 2017/9/9.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret;
    private int expiredIn;
    private String authHeader;
    private String authCookie;
}
