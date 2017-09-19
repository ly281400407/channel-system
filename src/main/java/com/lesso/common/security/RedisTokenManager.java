package com.lesso.common.security;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * reids token 管理器
 * @author ly
 * @date 20170919
 */
public class RedisTokenManager implements TokenManager{

    /**
     * 过期时间
     */
    private long expired;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    StringRedisTemplate stringRedisTemplate;

    @Override
    public String createToken(String username) {
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(token, username, expired, timeUnit);
        return token;
    }

    @Override
    public boolean checkToken(String username, String token) {

        //检查token是否过期
        long result = stringRedisTemplate.getExpire(token);
        if(result<=0){
            return false;
        }

        //检查token对应用户是否匹配
        String curUsername = stringRedisTemplate.opsForValue().get(token);
        if(!username.equals(curUsername)){
            return false;
        }

        //更新token到期时间
        stringRedisTemplate.expire(token, expired, timeUnit);
        return true;

    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
