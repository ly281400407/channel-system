package com.lesso.control;

import com.lesso.common.exception.TokenException;
import com.lesso.common.network.Response;
import com.lesso.common.security.IgnoreSecurity;
import com.lesso.pojo.AdminUser;
import com.lesso.pojo.Msg;
import com.lesso.pojo.TenantInfo;
import com.lesso.pojo.User;
import com.lesso.service.IAccountService;
import com.lesso.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by czx on 2017/9/26.
 */
@Controller
public class AccountController {
    @Resource
    IAccountService accountService;

    //注册租户
    @IgnoreSecurity(val = true)
    @RequestMapping(value = "/tenant",method = RequestMethod.POST)//method = RequestMethod.POST
    @ResponseBody
    public Response register(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Response responseResult=new Response();
        String userName=request.getParameter("accountName");
        String password=request.getParameter("pwd");
        String phone=request.getParameter("phone");
        String verificationCode=request.getParameter("verificationCode");
        TenantInfo tenantInfo=new TenantInfo();
        tenantInfo.setTenantAccount(userName);
        tenantInfo.setTenantPassword(password);
        tenantInfo.setPhoneNo(phone);
        tenantInfo.setVerificationCode(verificationCode);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.accountService.createUserInfo(tenantInfo);
            Boolean success=(Boolean) resultMap.get("isSuccess");
            if(success!=null&&success){
                responseResult.success(resultMap);
            }else{
                responseResult.failure((String)resultMap.get("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.failure();
        }
        return responseResult;
    }

    //完善租户信息
    @IgnoreSecurity(val = true)
    @RequestMapping(value = "/tenant",method = RequestMethod.PUT)//method = RequestMethod.PUT
    @ResponseBody
    public Response updateTenantInfo(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Response responseResult=new Response();
        //String tenantId=request.getParameter("tenantId");
        String tenantAccount=request.getParameter("tenantAccount");
        String companyName=request.getParameter("companyName");
        String province=request.getParameter("province");
        String city=request.getParameter("city");
        String county=request.getParameter("county");
        String address=request.getParameter("address");
        String tenantType=request.getParameter("tenantType");
        String email= request.getParameter("email");

        User user = new User();
       // user.setTenantId(Integer.valueOf(tenantId));
        user.setTenantAccount(tenantAccount);
        user.setUsername(tenantAccount);
        user.setCompanyName(companyName);
        user.setProvince(province);
        user.setCity(city);
        user.setCounty(county);
        user.setAddress(address);
        user.setEmail(email);

        AdminUser user1= new AdminUser();
        user1.setUserType(Long.valueOf(tenantType));
        user1.setUsername(tenantAccount);

        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.accountService.createUserDB(user1,user);
            Boolean success=(Boolean) resultMap.get("isSuccess");
            if(success!=null&&success){
                responseResult.success(resultMap);
            }else{
                responseResult.failure((String)resultMap.get("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.failure();
        }
        return responseResult;
    }

    //获取手机验证码   测试通过
    @IgnoreSecurity(val = true)
    @RequestMapping(value = "/verificationCode",method = RequestMethod.GET)
    @ResponseBody
    public Response getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Response responseResult=new Response();
        String phone=request.getParameter("phone");
        Msg msg=new Msg();
        msg.setPhoneNo(phone);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.accountService.getVerificationCode(msg);
            Boolean success=(Boolean) resultMap.get("isSuccess");
            if(success!=null&&success){
                responseResult.success(resultMap);
            }else{
                responseResult.failure((String)resultMap.get("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.failure();
        }
        return responseResult;
    }

    //用户登录
    @IgnoreSecurity(val = true)
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Response loginOfUser(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Response responseResult=new Response();
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String dbName=request.getParameter("dbName");
        User user=new User();
        user.setUsername(userName);
        user.setPassword(password);

        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("dbName",dbName);
        try{
            resultMap=this.accountService.loginOfUser(user);
            Boolean success=(Boolean) resultMap.get("isSuccess");
            if(success!=null&&success){
                responseResult.success(resultMap);
            }else{
                responseResult.failure((String)resultMap.get("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.failure();
        }
        return responseResult;
    }

    //租户登录
    @IgnoreSecurity(val = true)
    @RequestMapping(value = "/tenant",method = RequestMethod.GET)
    @ResponseBody
    public Response loginOfTenantInfo(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Response responseResult=new Response();
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        TenantInfo tenantInfo=new TenantInfo();
        tenantInfo.setTenantAccount(userName);
        tenantInfo.setTenantPassword(password);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.accountService.loginOfTenantInfo(tenantInfo);
            Boolean success=(Boolean) resultMap.get("isSuccess");
            if(success!=null&&success){
                responseResult.success(resultMap);
            }else{
                responseResult.failure((String)resultMap.get("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            responseResult.failure();
        }
        return responseResult;
    }

}
