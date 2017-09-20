package com.lesso.control;

import com.lesso.common.Exception.TokenException;
import com.lesso.common.security.IgnoreSecurity;
import com.lesso.service.TestService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @Resource
    TestService testService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @IgnoreSecurity(val = true)
    @RequestMapping("/hello")
    public String Save(HttpServletRequest request, HttpServletResponse response) throws TokenException { // user:视图层传给控制层的表单对象；model：控制层返回给视图层的对象
        System.out.println("hello word!");
        testService.testHello();
        //redisTemplate.opsForValue().set("userid","token",10000000, TimeUnit.MILLISECONDS);
        //redisTemplate.boundValueOps("userid").get();
        redisTemplate.delete("useid");
        stringRedisTemplate.opsForValue().set("userid", "token");
        stringRedisTemplate.expire("userid", 10000000, TimeUnit.MILLISECONDS);
        Long s = stringRedisTemplate.getExpire("userid");
        System.out.println(s);
        return "index";
    }

}
