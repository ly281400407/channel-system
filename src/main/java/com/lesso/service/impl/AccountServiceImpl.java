package com.lesso.service.impl;

import com.lesso.common.db.DataSourceHolder;
import com.lesso.common.util.DataBaseUtil;
import com.lesso.common.util.UUIDUtil;
import com.lesso.common.sms.SMSUtil;
import com.lesso.mapper.manager.MsgMapper;
import com.lesso.mapper.manager.ServerInfoMapper;
import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.mapper.user.UserMapper;
import com.lesso.pojo.*;
import com.lesso.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by czx on 2017/9/26.
 */
@Service("accountService")
/*@Transactional(propagation= Propagation.REQUIRED)*/
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private ServerInfoMapper serverInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private TenantInfoMapper tenantInfoMapper;

    @Override
    public TenantInfo getTenantInfo(String tenantAccount) {
        TenantInfo tenantInfo = tenantInfoMapper.getTenantByName(tenantAccount);
        return tenantInfo;
    }
    //校验验证码
   public Map checkVerificationCodePhone(Msg msg){
       Map<String,Object> resultMap=new HashMap<>();
       Msg msg1=this.msgMapper.getEffectiveMsg(msg);
       if(msg1!=null && msg.getVerificationCode()!=null){
           resultMap.put("isExist",true);
           resultMap.put("msg","该验证码正确");
       }else{
           resultMap.put("isExist",false);
           resultMap.put("msg","该验证码错误或者已过失效！");
       }
       return resultMap;

   }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Map createUserInfo( TenantInfo tenant) {
        Map<String,Object> resultMap=new HashMap<>();
        if(tenant!=null){
            //校验手机号是否重复
            resultMap=checkTenantPhoneUniqueness(tenant.getPhoneNo());
            Boolean isExist=(Boolean)resultMap.get("isExist");
            if(isExist){
                return resultMap;
            }
            //校验用户名是否重复
            resultMap=checkTenantNameUniqueness(tenant.getTenantAccount());
            Boolean isNameExist=(Boolean)resultMap.get("isExist");
            if(isNameExist){
                return resultMap;
            }
            //校验手机验证码正确和在有效期
            Msg msg=new Msg();
            msg.setPhoneNo(tenant.getPhoneNo());
            msg.setVerificationCode(tenant.getVerificationCode());
            resultMap=checkVerificationCodePhone(msg);
            Boolean msgFlag=(Boolean)resultMap.get("isExist");
            if(!msgFlag){
               return resultMap;
            }

            List<ServerInfo> serverInfos= serverInfoMapper.getRegisterServerInfo();

            if(serverInfos!=null && serverInfos.size()>0){
                    ServerInfo serverInfo=serverInfos.get(0);
                    tenant.setServerIp(serverInfo.getServerIp());
                    tenant.setServerPort(serverInfo.getServerPort());
                    tenant.setDbName("qudao_"+tenant.getTenantAccount());
                    tenant.setDbAccount("qudao_"+tenant.getTenantAccount());
                    tenant.setDbPassword("qudao_"+tenant.getTenantPassword());
                    tenant.setCompanyName("");
                    Date date=new Date();
                    tenant.setCreated(date);
                    tenant.setUpdated(date);
                    tenant.setStatus(2);
                    //保存租户服务器信息
                    serverInfoMapper.insertTenantInfo(tenant);
                    //更新业务服务器租户信息
                    serverInfoMapper.updateQDServerInfo(serverInfo.getServerIp());
                     AdminUser user= new AdminUser();
                     user.setPhoneNo(tenant.getPhoneNo());
                     Date now =new Date();
                     user.setCreateTime(now);
                     user.setDelflag(0);
                     user.setLastLoginTime(now);
                     user.setPassword(tenant.getTenantPassword());
                     user.setStatus(2);
                     user.setUpdateTime(now);
                     user.setUsername(tenant.getTenantAccount());
                     user.setUserType(1L);
                    //保存管理服务器用户信息
                     serverInfoMapper.insertUser(user);
                    resultMap.put("msg","您的账号注册成功");
                    resultMap.put("isCreateSuccess",true);
            }else{
                    resultMap.put("msg","服务器用户量超量，请稍后再试");
                    resultMap.put("isCreateSuccess",false);
            }

        }else{
            tenant=new TenantInfo();
            resultMap.put("msg","您的注册信息丢失，请重新进行注册");
            resultMap.put("isCreateSuccess",false);
        }
        resultMap.put("tenant",tenant);
        return resultMap;
    }

    public Map checkTenantPhoneUniqueness(String phone){
        Map<String,Object> resultMap=new HashMap<>();
      TenantInfo tenantInfo=this.tenantInfoMapper.getTenantByPhone(phone);
        if(tenantInfo!=null && tenantInfo.getPhoneNo()!=null){
            resultMap.put("isExist",true);
            resultMap.put("msg","该手机号已被注册，请更换新号码重新注册");
        }else{
            resultMap.put("isExist",false);
            resultMap.put("msg","该手机号可以进行注册");
        }
        return  resultMap;
    }

    public Map checkTenantNameUniqueness(String tenantAccount){
        Map<String,Object> resultMap=new HashMap<>();
        TenantInfo tenantInfo = tenantInfoMapper.getTenantByName(tenantAccount);
        if(tenantInfo!=null && tenantInfo.getPhoneNo()!=null){
            resultMap.put("isExist",true);
            resultMap.put("msg","该用户名已被注册，请更换新号码重新注册");
        }else{
            resultMap.put("isExist",false);
            resultMap.put("msg","该用户名可以进行注册");
        }
        return  resultMap;
    }




    @Override
    public Map createUserDB(AdminUser adminUser,User user) {
        Map<String,Object> resultMap=new HashMap<>();


        DataSourceHolder.setDataSource(null);

        TenantInfo tenant= this.tenantInfoMapper.getTenantByIdOrName(user.getTenantId(),user.getTenantAccount());

        //创建数据库操作
        DataSourceHolder.setDataSource(DataBaseUtil.getRootOfBusinessDB(tenant.getServerIp()));
        Map<String,Object> map=new HashMap<>();
        String creatDB="create database "+tenant.getDbName();
        map.put("sql",creatDB);
        this.serverInfoMapper.executeCreateQuery(map);
        String creatUser="create user '"+tenant.getDbAccount()+"'@'%' identified by '"+tenant.getDbPassword()+"'";
        map.put("sql",creatUser);
        this.serverInfoMapper.executeCreateQuery(map);

        String grantUser="grant select,insert,update,delete,create on "+tenant.getDbName()+".* to "+tenant.getDbAccount();
        map.put("sql",grantUser);
        this.serverInfoMapper.executeCreateQuery(map);

        map.put("sql","flush  privileges");
        this.serverInfoMapper.executeCreateQuery(map);


        DataSourceHolder.setDataSource(null);
        //激活租户用户
        adminUser.setStatus(1);
        this.serverInfoMapper.updateAdminUserStatus(adminUser);
        //激活租户数据库
        tenant.setStatus(1);
        this.tenantInfoMapper.updateTenantStatus(tenant);


        DataSourceHolder.setDataSource(tenant.getDbName());

        String createTable= "CREATE TABLE IF NOT EXISTS  `User` (\n" +
                "  `id` int(10) NOT NULL AUTO_INCREMENT,\n" +
                "  `tenantAccount` varchar(20) NOT NULL DEFAULT '' COMMENT '租户账号',\n" +
                "  `username` varchar(20)  NOT NULL DEFAULT '' COMMENT '用户账号',\n" +
                "  `password` varchar(50)  NOT NULL DEFAULT '' COMMENT '密码',\n" +
                "  `userType` tinyint(2)  NOT NULL DEFAULT '0' COMMENT '用户类型-1商户-2商户员工',\n" +
                "  `phoneNo` varchar(200)  COMMENT '手机号码',\n" +
                "  `email` varchar(50)   COMMENT '邮箱',\n" +
                "  `name` varchar(15)   COMMENT '姓名',\n" +
                "  `birthday` datetime  COMMENT '生日',\n" +
                "  `idNo` varchar(18)   COMMENT '身份证',\n" +
                "  `companyName` varchar(15)  COMMENT '公司名',\n" +
                "  `departmentNo` varchar(15)  COMMENT '部门编号',\n" +
                "  `departmentName` varchar(50)   COMMENT '部门名称',\n" +
                "  `placeOfOrigin` varchar(50)  COMMENT '籍贯',\n" +
                "  `province` varchar(50) COMMENT '地址',\n" +
                "  `city` varchar(50) COMMENT '地址',\n" +
                "  `county` varchar(50) COMMENT '地址',\n" +
                "  `address` varchar(50) COMMENT '地址',\n" +
                "  `abbreviation` varchar(50) COMMENT '拼音简称',\n" +
                "  `entryTime` datetime COMMENT '入职时间',\n" +
                "  `quitTime` datetime COMMENT '离职时间',\n" +
                "  `registerTime` datetime NOT NULL COMMENT '注册时间',\n" +
                "  `lastLoginTime` datetime COMMENT '最后一次登录时间' ,\n" +
                "  `createTime` datetime NOT NULL COMMENT '创建时间',\n" +
                "  `updateTime` datetime NOT NULL COMMENT '更新时间',\n" +
                "  `delflag` int NOT NULL DEFAULT '1' COMMENT '删除标志',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4;";

        map.put("sql",createTable);
        this.serverInfoMapper.executeCreateQuery(map);

        Date now = new Date();
        user.setPassword(tenant.getTenantPassword());
        user.setLastLoginTime(now);
        user.setCreateTime(now);
        user.setRegisterTime(now);
        user.setLastLoginTime(now);
        user.setUserType(1L);
        user.setDelflag(0);
        user.setPhoneNo(tenant.getPhoneNo());

        user.setCreateTime(now);
        user.setUpdateTime(now);

        //保存租户的信息保存到业务用户表中
        this.userMapper.insertUser(user);
        resultMap.put("dbName",tenant.getDbName());
        resultMap.put("user",user);
        resultMap.put("msg","完善用户信息成功");

        return resultMap;
    }

    @Override
    public Map loginOfTenantInfo(TenantInfo tenant) {
        Map<String,Object> resultMap=new HashMap<>();
        TenantInfo tenantInfo=this.serverInfoMapper.getUserInfo(tenant);

        if(tenantInfo!=null && tenantInfo.getId()!=null){
            resultMap.put("islogin",true);
            resultMap.put("tenant",tenant);
            resultMap.put("dbName",tenantInfo.getDbName());
            resultMap.put("msg","登录成功");
        }else{
            resultMap.put("islogin",false);
            resultMap.put("tenant",new TenantInfo());
            resultMap.put("msg","登录失败");
        }
        return resultMap;
    }

    @Override
    public Map loginOfUser(User user) {
        Map<String,Object> resultMap=new HashMap<>();
        User user1=this.userMapper.getUserInfo(user);
        if(user1!=null && user1.getId()!=null){
            resultMap.put("islogin",true);
            resultMap.put("user",user1);
            resultMap.put("msg","登录成功");
        }else{
            resultMap.put("islogin",false);
            resultMap.put("user",user);
            resultMap.put("msg","登录失败");
        }
        return resultMap;
    }



    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public Map getVerificationCode(Msg msg) {
        String verificationCode=UUIDUtil.getNumUUID(4);
        msg.setVerificationCode(verificationCode);
        Map<String,Object> resultMap=new HashMap<>();
        if(SMSUtil.sendCodeMsg(msg.getPhoneNo(),verificationCode)){
            Calendar nowTime = Calendar.getInstance();
            msg.setCreateTime(nowTime.getTime());
            nowTime.add(Calendar.MINUTE, 5);
            msg.setInvalidTime(nowTime.getTime());
            msg.setType(0);
            msg.setDelflag(0);
            this.msgMapper.insertMsg(msg);
            resultMap.put("isSend",true);
            resultMap.put("verificationCode",msg);
            resultMap.put("msg","发送验证码成功,请尽快填写");
        }else{
            resultMap.put("isSend",false);
            resultMap.put("verificationCode",msg);
            resultMap.put("msg","发送验证码失败，请重新获取");
        }
        return resultMap;
    }

}
