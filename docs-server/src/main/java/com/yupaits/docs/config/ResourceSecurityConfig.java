package com.yupaits.docs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Configuration
@EnableResourceServer
public class ResourceSecurityConfig extends ResourceServerConfigurerAdapter {

    private final ObjectMapper objectMapper;

    @Autowired
    public ResourceSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.UNAUTHORIZED));
                }))
                .and()
                .authorizeRequests()
                .antMatchers("/doc.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }
}
