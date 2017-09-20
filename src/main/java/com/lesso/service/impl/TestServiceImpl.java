package com.lesso.service.impl;

import com.lesso.mapper.user.UserMapper;
import com.lesso.pojo.User;
import com.lesso.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService{

    @Autowired
    private UserMapper userMapper;

    public String testHello() {
        try {
            User user = userMapper.get(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Hello,this is hello service!";
    }
}
