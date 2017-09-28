package com.lesso.control;

import com.lesso.common.db.DataSourceHolder;
import com.lesso.common.exception.TokenException;
import com.lesso.common.security.IgnoreSecurity;
import com.lesso.common.util.MultipartFileUtil;
import com.lesso.pojo.Finance;
import com.lesso.pojo.TenantInfo;
import com.lesso.pojo.User;
import com.lesso.service.TestService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Resource
    TestService testService;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @IgnoreSecurity(val = true)
    @RequestMapping("/loginOfTenantPage")
    public String loginOfTenantPage(){
        return "loginOfTenant";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/loginOfUserPage")
    public String loginOfUserPage(){
        return "loginOfUser";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/registerPage")
    public String registerPage(){
        return "register";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/displayPage")
    public String displayPage(){
        return "display";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/addUserPage")
    public String addUserPage(){
        return "addUser";
    }


    @IgnoreSecurity(val = true)
    @RequestMapping("/displayFinancePage")
    public String displayFinancePage(){
        return "displayFinance";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/addFinancePage")
    public String addFinancePage(){
        return "addFinance";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/uploadPage")
    public String uploadPage(){
        return "upload";
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/test")
    @ResponseBody
    public User test(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String dataType=request.getParameter("dataType");
        if("1".equals(dataType)){
            DataSourceHolder.setDataSource("dataSource1");
        }else {
            DataSourceHolder.setDataSource("dataSource2");
        }
        User user=testService.test();
        //System.out.println(user.name);
        return user;
    }


//    @IgnoreSecurity(val = true)
//    @RequestMapping("/addDB")
//    public String addDataSource(HttpServletRequest request, HttpServletResponse response,String userId) throws TokenException { // user:视图层传给控制层的表单对象；model：控制层返回给视图层的对象
//        DataBaseInfo dataBaseInfo=new DataBaseInfo();
//        dataBaseInfo.setDbKey("dataSource1");
//        dataBaseInfo.setUrl("jdbc:mysql://118.89.64.133:2208/qudao_001?serverTimezone=GMT");
//        dataBaseInfo.setUserName("qudao");
//        dataBaseInfo.setPassword("qudao!@#$%^&*");
//        DataBaseInfo dataBaseInfo2=new DataBaseInfo();
//        dataBaseInfo2.setDbKey("dataSource2");
//        dataBaseInfo2.setUrl("jdbc:mysql://139.199.62.80:2208/qudao_008?serverTimezone=GMT");
//        dataBaseInfo2.setUserName("qudao");
//        dataBaseInfo2.setPassword("qudao!@#$%^&*");
//        testService.addDB(dataBaseInfo);
//        testService.addDB(dataBaseInfo2);
//        return "index";
//    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/register")
    @ResponseBody
    public Map register(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String companyName=request.getParameter("companyName");
        String phone=request.getParameter("phone");
        TenantInfo tenantInfo=new TenantInfo();
        tenantInfo.setTenantAccount(userName);
        tenantInfo.setTenantPassword(password);
        tenantInfo.setCompanyName(companyName);
        tenantInfo.setPhoneNo(phone);

        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.createUserInfo(tenantInfo);
            boolean isCreateUser=(boolean)resultMap.get("isCreateUser");
            TenantInfo tenantInfo1=(TenantInfo)resultMap.get("tenant");
            if(isCreateUser){
                resultMap=this.testService.createUserDB(tenantInfo1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;

    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/loginOfTenant")
    @ResponseBody
    public Map loginOfTenantInfo(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        TenantInfo tenantInfo=new TenantInfo();
        tenantInfo.setTenantAccount(userName);
        tenantInfo.setTenantPassword(password);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.loginOfTenantInfo(tenantInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/loginOfUser")
    @ResponseBody
    public Map loginOfUser(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String dbName=request.getParameter("dbName");
        User user=new User();
        user.setName(userName);
        user.setPassword(password);
        user.setDbName(dbName);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.loginOfUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }


    @IgnoreSecurity(val = true)
    @RequestMapping("/queryUser")
    @ResponseBody
    public Map queryUser(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Map<String,Object> resultMap=new HashMap<>();
        String searchText=request.getParameter("searchText");
        String tenantId=request.getParameter("tenantId");
        User user=new User();
        user.setSearchText(searchText);
        user.setTenantId(Integer.valueOf(tenantId));
        try{
            resultMap=this.testService.getUserList(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/queryAllTenant")
    @ResponseBody
    public Map queryAllTenant(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.getAllTenant();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @IgnoreSecurity(val = true)
    @RequestMapping("/addUser")
    @ResponseBody
    public Map addUser(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String dbName=request.getParameter("dbName");
        String companyName=request.getParameter("companyName");
        String tenantId=request.getParameter("tenantId");
        User user=new User();
        user.setName(userName);
        user.setPassword(password);
        user.setDbName(dbName);
        user.setCompanyName(companyName);
        user.setSex(1);
        user.setTenantId(Integer.valueOf(tenantId));
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.addUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }


    @IgnoreSecurity(val = true)
    @RequestMapping("/addUserInDiff")
    @ResponseBody
    public Map addUserInDiff(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        String dbName=request.getParameter("dbName");
        String companyName=request.getParameter("companyName");
        String tenantId=request.getParameter("tenantId");
        User user=new User();
        user.setName(userName);
        user.setPassword(password);
        user.setDbName(dbName);
        user.setCompanyName(companyName);
        user.setTenantId(Integer.valueOf(tenantId));
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.insertUserDifferentTable(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }
    @IgnoreSecurity(val = true)
    @RequestMapping("/queryUserInDiff")
    @ResponseBody
    public Map queryUserInDiff(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String searchText=request.getParameter("searchText");
        String tenantId=request.getParameter("tenantId");
        User user=new User();
        user.setTenantId(Integer.valueOf(tenantId));
        user.setSearchText(searchText);
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.queryUserDifferentTable(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }



    @IgnoreSecurity(val = true)
    @RequestMapping("/addFinanceInDiff")
    @ResponseBody
    public Map addFinanceInDiff(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String content=request.getParameter("content");
        String type=request.getParameter("type");
        String status=request.getParameter("status");
        String dbName=request.getParameter("dbName");
        String fromId=request.getParameter("fromId");

        Finance finance=new Finance();
        finance.setContent(content);
        finance.setType(Integer.valueOf(type));
        finance.setDbName(dbName);
        finance.setStatus(Integer.valueOf(status));
        finance.setFromId(Integer.valueOf(fromId));

        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.insertFinanceDifferentTable(finance);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }
    @IgnoreSecurity(val = true)
    @RequestMapping("/queryFinanceInDiff")
    @ResponseBody
    public Map queryFinanceInDiff(HttpServletRequest request, HttpServletResponse response) throws TokenException {
        String searchText=request.getParameter("searchText");
        String fromId=request.getParameter("fromId");
        Finance finance=new Finance();
        finance.setSearchText(searchText);
        finance.setFromId(Integer.valueOf(fromId));
        Map<String,Object> resultMap=new HashMap<>();
        try{
            resultMap=this.testService.queryFinanceDifferentTable(finance);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }




        //上传图片品牌
        @IgnoreSecurity(val = true)
        @RequestMapping(value="/uploadPic")
        @ResponseBody
        public Map uploadPic(@RequestParam(required=false)MultipartFile picture, HttpServletResponse response) throws Exception{
           // picture.getBytes();
            String path = testService.uploadPic(MultipartFileUtil.multipartToByte(picture).getPath(), picture.getOriginalFilename(), picture.getSize());
            //path:group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
            //url:http://192.168.200.128 (Linux 虚拟机的ip地址)
            String url ="192.168.149.135/" + path;
            Map<String,Object> resultMap=new HashMap<>();
            resultMap.put("url", url);
            resultMap.put("path", path);
            return resultMap;
        }



}
