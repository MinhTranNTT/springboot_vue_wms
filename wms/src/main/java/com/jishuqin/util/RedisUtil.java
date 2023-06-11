package com.jishuqin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final long DEFAULT_EXPIRE_DAYS = 1;

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设值
     *
     * @param key
     * @param value
     */
    public void setValue(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设值
     * <p>
     * 可以设置失效时间
     *
     * @param key
     * @param value
     */
    public void setValue(Object key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value);
        if (time == null || time <= 0) {
            time = DEFAULT_EXPIRE_DAYS;
        }
        redisTemplate.expire(key, time, TimeUnit.DAYS);
    }

    /**
     * 删除数据
     *
     * @param key
     */
    public void delete(Object key) {
        redisTemplate.delete(key);
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(Object key) {
        return redisTemplate.hasKey(key);
    }
}
