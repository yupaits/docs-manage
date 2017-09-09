package com.yupaits.docs.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据库配置类
 * Created by yupaits on 2017/8/5.
 */
@MapperScan(basePackages = "com.yupaits.docs.mapper")
@Configuration
public class DruidConfig {

    @Autowired
    private DruidProperties druidConfig;

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(druidConfig.mysqlDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/**.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        servletRegistrationBean.addInitParameter("deny", "");
        servletRegistrationBean.addInitParameter("username", "admin");
        servletRegistrationBean.addInitParameter("password", "admin");
        return servletRegistrationBean;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(druidConfig.mysqlDataSource());
    }
}
