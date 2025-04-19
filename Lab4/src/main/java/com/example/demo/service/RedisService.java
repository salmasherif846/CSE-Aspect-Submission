package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> valueOps;

    @Autowired
    public RedisService(StringRedisTemplate template) {
        this.redisTemplate = template;
        this.valueOps = template.opsForValue();
    }

    public void put(String key, String data) {
        valueOps.set(key, data);
    }

    public void put(String key, String data, Duration expiration) {
        valueOps.set(key, data, expiration);
    }

    public String fetch(String key) {
        return valueOps.get(key);
    }

    public Boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean acquireLock(String key, String token, Duration expiration) {
        return valueOps.setIfAbsent(key, token, expiration);
    }

    public Long incrementAndSetExpiry(String key, Duration expiration) {
        Long value = valueOps.increment(key);
        if (value != null && value == 1) {
            redisTemplate.expire(key, expiration);
        }
        return value;
    }
}
