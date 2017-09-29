package com.lesso.service.impl;

import com.lesso.common.db.DataBaseInfo;
import com.lesso.common.db.DataSourceHolder;
import com.lesso.common.db.DynamicDataSource;
import com.lesso.common.util.DataBaseUtil;
import com.lesso.common.util.FastDFSUtils;
import com.lesso.common.util.SpringContextUtil;
import com.lesso.mapper.manager.FinanceMapper;
import com.lesso.mapper.manager.ServerInfoMapper;
import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.mapper.user.UserMapper;
import com.lesso.pojo.*;
import com.lesso.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by czx on 2017/9/26.
 */
@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private ServerInfoMapper serverInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FinanceMapper financeMapper;
    @Autowired
    private TenantInfoMapper tenantInfoMapper;

    @Override
    public TenantInfo getTenantInfo(String tenantAccount) {
        TenantInfo tenantInfo = tenantInfoMapper.getTenantInfo(tenantAccount);
        return tenantInfo;
    }

    @Override
    public Map createUserInfo( TenantInfo tenant) {
        Map<String,Object> resultMap=new HashMap<>();
        if(tenant!=null){
            TenantInfo tenant1 = serverInfoMapper.getUserDBInfo(tenant.getCompanyName());
            if(tenant1==null || tenant1.getId()==null){
                List<ServerInfo> serverInfos= serverInfoMapper.getRegisterServerInfo();
                if(serverInfos!=null && serverInfos.size()>0){
                    ServerInfo serverInfo=serverInfos.get(0);
                    tenant.setServerIp(serverInfo.getServerIp());
                    tenant.setServerPort(serverInfo.getServertPort());
                    tenant.setDbName("qudao_"+tenant.getTenantAccount());
                    tenant.setDbAccount("qudao_"+tenant.getTenantAccount());
                    tenant.setDbPassword("qudao_"+tenant.getTenantPassword());
                    serverInfoMapper.insertUser(tenant);
                    serverInfoMapper.updateQDServerInfo(serverInfo.getServerIp());
                    resultMap.put("isCreateUser",true);
                }else{
                    resultMap.put("msg","server is full!");
                    resultMap.put("isCreateUser",false);
                }

            }else{
                tenant=tenant1;
                resultMap.put("isCreateUser",false);
            }
        }else{
            tenant=new TenantInfo();
        }
        resultMap.put("tenant",tenant);
        resultMap.put("msg","save userinfo in adminDB");
        return resultMap;
    }

    @Override
    public Map createUserDB(TenantInfo tenant) {
        Map<String,Object> resultMap=new HashMap<>();

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


        DataSourceHolder.setDataSource(tenant.getDbName());

        String createTable="CREATE TABLE `t_user` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) DEFAULT NULL,\n" +
                "  `password` varchar(255) DEFAULT NULL,\n" +
                "  `company` varchar(255) DEFAULT NULL,\n" +
                "  `sex` int(255) DEFAULT NULL,\n" +
                " `tenant_id` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ")";
        map.put("sql",createTable);
        this.serverInfoMapper.executeCreateQuery(map);

        for(int i=0;i<8;i++){
            String createDiffTable="CREATE TABLE `QDFinance_"+i+"` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `fromId` int(11) unsigned NOT NULL COMMENT '使用用户的id',\n" +
                    "  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '数据内容',\n" +
                    "  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '数据类型',\n" +
                    "  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',\n" +
                    "  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',\n" +
                    "  `created` int(11) unsigned NOT NULL COMMENT '创建时间',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=2108171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin" ;
            map.put("sql",createDiffTable);
            this.serverInfoMapper.executeCreateQuery(map);
        }


        User user=new User();
        user.setName(tenant.getTenantAccount());
        user.setPassword(tenant.getTenantPassword());
        user.setCompanyName(tenant.getCompanyName());
        user.setSex(1);
        user.setTenantId(tenant.getId());
        this.userMapper.insertUser(user);

        resultMap.put("tenant",tenant);

        resultMap.put("msg","create userdb successlly");

        return resultMap;
    }

    @Override
    public Map loginOfTenantInfo(TenantInfo tenant) {
        Map<String,Object> resultMap=new HashMap<>();
        TenantInfo tenantInfo=this.serverInfoMapper.getUserInfo(tenant);
        if(tenantInfo!=null && tenantInfo.getId()!=null){
            resultMap.put("islogin",true);
            resultMap.put("tenant",tenantInfo);
            resultMap.put("msg","login successlly");
        }else{
            resultMap.put("islogin",false);
            resultMap.put("tenant",new TenantInfo());
            resultMap.put("msg","login fail");
        }
        return resultMap;
    }

    @Override
    public Map loginOfUser(User user) {
        Map<String,Object> resultMap=new HashMap<>();
        User user1=this.userMapper.getUserInfo(user);
        if(user1!=null && user1.getId()!=null){
            user1.setDbName(user.getDbName());
            resultMap.put("islogin",true);
            resultMap.put("user",user1);
            resultMap.put("msg","login successlly");
        }else{
            resultMap.put("islogin",false);
            resultMap.put("user",user);
            resultMap.put("msg","login fail");
        }
        return resultMap;
    }

    @Override
    public Map addUser(User user) {
        Map<String,Object> resultMap=new HashMap<>();
        int rows=this.userMapper.insertUser(user);
        resultMap.put("user",user);
        resultMap.put("msg","save user successlly");
        return resultMap;
    }

    @Override
    public Map getMsg(Msg msg) {
        return null;
    }

}
