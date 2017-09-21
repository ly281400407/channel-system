package com.lesso.service.impl;

import com.lesso.common.data.AbstractMapperFacade;
import com.lesso.mapper.user.UserMapper;
import com.lesso.pojo.User;
import com.lesso.service.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("testService")
public class TestServiceImpl implements TestService{

    private Logger logger = Logger.getLogger(TestServiceImpl.class);

    @Resource
    AbstractMapperFacade mapperFacade;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public String testHello() throws Exception {
        UserMapper userMapper = mapperFacade.getMapper("102230", UserMapper.class);
        User user = userMapper.get(1);
        logger.info(user.getName());
        return "Hello,this is hello service!";
    }
}
