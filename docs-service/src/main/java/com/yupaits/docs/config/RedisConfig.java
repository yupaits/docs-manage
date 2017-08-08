package com.yupaits.docs.config;

import com.yupaits.docs.utils.redis.RedisSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yupaits on 2017/8/8.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisSupport redisSupport() {
        return new RedisSupport();
    }
}
