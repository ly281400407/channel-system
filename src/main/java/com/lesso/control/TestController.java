package com.lesso.control;

import com.lesso.common.security.IgnoreSecurity;
import com.lesso.pojo.TenantInfo;
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
    @RequestMapping("/testForSub")
    public String testForSub(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("hello word!");
        testService.test();
        return "index";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/testForMain")
    public String testForMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("hello word!");
        TenantInfo tenantInfo = testService.getTenantInfo("liyou_001");
        return "index";
    }

}
