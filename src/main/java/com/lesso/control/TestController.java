package com.lesso.control;

import com.lesso.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

    @Resource
    TestService testService;

    @RequestMapping("/hello")
    public String Save(HttpServletRequest request, HttpServletResponse response) { // user:视图层传给控制层的表单对象；model：控制层返回给视图层的对象
        System.out.println("hello word!");
        testService.testHello();
        return "index";
    }

}
