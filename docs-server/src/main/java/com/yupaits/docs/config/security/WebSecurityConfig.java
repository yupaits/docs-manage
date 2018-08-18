package com.yupaits.docs.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.common.constants.DocsConsts;
import com.yupaits.docs.common.constants.EncryptConsts;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final JwtHelper jwtHelper;
    private final RedisTemplate redisTemplate;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(ObjectMapper objectMapper, JwtHelper jwtHelper, RedisTemplate redisTemplate,
                             UserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
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

    @Bean
    public TokenAuthFilter tokenAuthFilter() {
        return new TokenAuthFilter(objectMapper, jwtHelper, redisTemplate, userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Accept", "Content-Type", "Origin", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList(DocsConsts.AUTH_HEADER_NAME));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.FORBIDDEN));
                }))
                .and().addFilterBefore(tokenAuthFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(DocsConsts.ignorePaths).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    User user = (User) authentication.getPrincipal();
                    String token = jwtHelper.generateToken(user.getUsername());
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    objectMapper.writeValue(response.getWriter(), Result.ok(token));
                })
                .failureHandler(((request, response, exception) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    objectMapper.writeValue(response.getWriter(), Result.fail(ResultCode.UNAUTHORIZED));
                }));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()).userDetailsService(userDetailsService);
    }
}
