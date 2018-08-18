package com.yupaits.docs.config;

import com.yupaits.docs.common.constants.DocsConsts;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yupaits
 * @date 2018/8/11
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Accept", "Content-Type", "Origin", "Authorization")
                .allowCredentials(false)
                .exposedHeaders(DocsConsts.AUTH_HEADER_NAME);
    }
}
