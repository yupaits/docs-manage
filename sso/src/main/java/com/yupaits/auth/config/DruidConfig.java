package com.yupaits.auth.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据库配置类
 * Created by yupaits on 2017/8/5.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.yupaits.auth.repository")
public class DruidConfig {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    public DataSource dataSource() throws Exception {
        return druidProperties.mysqlDataSource();
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        servletRegistrationBean.addInitParameter("deny", "");
        servletRegistrationBean.addInitParameter("username", "admin");
        servletRegistrationBean.addInitParameter("password", "123456");
        return servletRegistrationBean;
    }
}
