package com.yupaits.docs.utils.redis;

import com.yupaits.docs.common.constant.ApplicationConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis工具类
 * Created by yupaits on 2017/8/8.
 */
@SuppressWarnings("unchecked")
public class RedisSupport {
    @Autowired
    private RedisTemplate redisTemplate;

    public void put(String key, Object object) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.opsForHash().putIfAbsent(ApplicationConstant.DOCS_DATA, key, object);
        }
    }

    public Object get(String key) {
        if (StringUtils.isNotBlank(key)) {
            return redisTemplate.opsForHash().get(ApplicationConstant.DOCS_DATA, key);
        }
        return null;
    }

    public void delete(String key) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.opsForHash().delete(ApplicationConstant.DOCS_DATA, key);
        }
    }
}
