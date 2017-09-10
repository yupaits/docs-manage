package com.yupaits.docs.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.config.jwt.JwtHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public JwtHelper jwtHelper;

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
        securityManager.setSessionManager(sessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filterMap = new HashMap<>();
        StatelessAuthcFilter statelessAuthcFilter = new StatelessAuthcFilter(objectMapper, jwtHelper);
        filterMap.put("jwtAuthc", statelessAuthcFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChains = new LinkedHashMap<>();
        filterChains.put("/", "anon");
        filterChains.put("/favicon.ico", "anon");
        filterChains.put("/**/*.html", "anon");
        filterChains.put("/**/*.css", "anon");
        filterChains.put("/**/*.js", "anon");
        filterChains.put("/**/*.eot", "anon");
        filterChains.put("/**/*.svg", "anon");
        filterChains.put("/**/*.ttf", "anon");
        filterChains.put("/**/*.woff*", "anon");
        filterChains.put("/images/*", "anon");
        filterChains.put("/auth/login", "anon");
        filterChains.put("/auth/logout", "anon");
        filterChains.put("/auth/register", "anon");
//        filterChains.put("/api/projects/**", "roles[ADMIN]");
        filterChains.put("/**", "jwtAuthc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChains);

        return shiroFilterFactoryBean;
    }
}
