package com.printrevo.tech.userservice.config.security;

import com.printrevo.tech.commonsecurity.dto.AccessToken;
import com.printrevo.tech.commonsecurity.helpers.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class CacheProvider implements Cache<String, AccessToken> {

    private final RedisTemplate<String, AccessToken> redisTemplate;

    public CacheProvider(RedisTemplate<String, AccessToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean contains(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public AccessToken getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(String key, AccessToken accessToken, long expiredIn, TimeUnit timeUnit) {
        var boundValueOperations = redisTemplate.boundValueOps(key);
        boundValueOperations.set(accessToken);
        boundValueOperations.expire(expiredIn, TimeUnit.MINUTES);
    }
}
