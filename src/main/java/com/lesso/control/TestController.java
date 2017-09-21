package com.lesso.control;

import com.lesso.common.security.IgnoreSecurity;
import com.lesso.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @Resource
    TestService testService;

    @IgnoreSecurity(val = true)
    @RequestMapping("/hello")
    public String Save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("hello word!");
        testService.testHello();
        return "index";
    }

}
