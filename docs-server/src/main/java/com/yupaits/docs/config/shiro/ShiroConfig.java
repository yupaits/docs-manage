package com.yupaits.docs.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.config.JwtProperties;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置
 * Created by ts495 on 2017/9/9.
 */
@Configuration
public class ShiroConfig {

    private final ObjectMapper objectMapper;
    private final JwtHelper jwtHelper;
    private final RedisTemplate redisTemplate;

    @Autowired
    public ShiroConfig(ObjectMapper objectMapper, JwtHelper jwtHelper, RedisTemplate redisTemplate) {
        this.objectMapper = objectMapper;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public StatelessRealm statelessRealm() {
        StatelessRealm statelessRealm = new StatelessRealm();
        statelessRealm.setCachingEnabled(false);
        return statelessRealm;
    }

    @Bean
    public StatelessSubjectFactory subjectFactory() {
        return new StatelessSubjectFactory();
    }

    @Bean
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        DefaultSubjectDAO defaultSubjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = (DefaultSessionStorageEvaluator) defaultSubjectDAO.getSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        securityManager.setRealm(statelessRealm());
        securityManager.setSubjectFactory(subjectFactory());
        securityManager.setRememberMeManager(null);
        securityManager.setSessionManager(sessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filterMap = new HashMap<>();
        StatelessAuthcFilter statelessAuthcFilter = new StatelessAuthcFilter(objectMapper, jwtHelper, redisTemplate);
        filterMap.put("jwtAuthc", statelessAuthcFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChains = new LinkedHashMap<>();
//        filterChains.put("/api/projects/**", "roles[ADMIN]");
        filterChains.put("/doc.html", "anon");
        filterChains.put("/v2/api-docs", "anon");
        filterChains.put("/swagger-resources/**", "anon");
        filterChains.put("/webjars/**", "anon");
        filterChains.put("/login", "anon");
        filterChains.put("/register", "anon");
        filterChains.put("/**", "jwtAuthc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChains);

        return shiroFilterFactoryBean;
    }
}
