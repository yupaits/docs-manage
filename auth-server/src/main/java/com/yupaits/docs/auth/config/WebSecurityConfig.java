package com.yupaits.docs.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.auth.entity.User;
import com.yupaits.docs.common.constants.EncryptConsts;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(ObjectMapper objectMapper, UserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(EncryptConsts.SHA_NUMBER);
        passwordEncoder.setIterations(EncryptConsts.PASSWORD_ENCODER_ITERATIONS);
        return passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        provider.setSaltSource(user -> ((User) user).getCredential());
        return provider;
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
                .antMatchers("/doc.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**", "/",
                        "/login", "/js/*.js", "/css/*.css", "*.png").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()).userDetailsService(userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
