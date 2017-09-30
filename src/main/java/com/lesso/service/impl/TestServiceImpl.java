package com.lesso.service.impl;

import com.lesso.common.db.AbstractUserDbCreator;
import com.lesso.common.db.DataBaseInfo;
import com.lesso.common.db.DataSourceHolder;
import com.lesso.common.util.FastDFSUtils;
import com.lesso.mapper.manager.FinanceMapper;
import com.lesso.mapper.manager.ServerInfoMapper;
import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.mapper.user.UserMapper;
import com.lesso.pojo.*;
import com.lesso.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    private ServerInfoMapper serverInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FinanceMapper financeMapper;
    @Autowired
    private TenantInfoMapper tenantInfoMapper;
    @Autowired
    private AbstractUserDbCreator userDbCreator;

    public User test() {
        User user=new User();
        try {
            user= userMapper.get(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

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
                    tenant.setServerPort(serverInfo.getServerPort());

//                    tenant.setDbName(ServerUtil.getDataName(serverInfo.getServerIp()));
//                    tenant.setDbAccount("root");
//                    tenant.setDbPassword("lesso@2128");

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

        DataBaseInfo dataBaseInfo=new DataBaseInfo();
        dataBaseInfo.setDbKey(tenant.getServerIp());
        dataBaseInfo.setUrl("jdbc:mysql://"+tenant.getServerIp()+":"+tenant.getServerPort()+"/qudao?serverTimezone=GMT&autoReconnect=true");
        dataBaseInfo.setUserName("root");
        dataBaseInfo.setPassword("lesso@2128");
        addDB(dataBaseInfo);
        DataSourceHolder.setDataSource(tenant.getServerIp());

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

        DataBaseInfo dataBaseInfo1=new DataBaseInfo();
        dataBaseInfo1.setDbKey(tenant.getDbName());
        dataBaseInfo1.setUrl("jdbc:mysql://"+tenant.getServerIp()+":"+tenant.getServerPort()+"/"+tenant.getDbName()+"?serverTimezone=GMT");
        dataBaseInfo1.setUserName(tenant.getDbAccount());
        dataBaseInfo1.setPassword(tenant.getDbPassword());
        addDB(dataBaseInfo1);

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
        /*user.setSex(1);*/
        user.setTenantId(tenant.getId());
        this.userMapper.insertUser(user);

        resultMap.put("tenant",tenant);

        resultMap.put("msg","create userdb successlly");

        return resultMap;
    }


//    @Override
//    public Map createUserDB(TenantInfo tenant) {
//        Map<String,Object> resultMap=new HashMap<>();
//
//        DataSourceHolder.setDataSource(tenant.getDbName());
//
//        Map<String,Object> map=new HashMap<>();
//        String createTable="CREATE TABLE If Not Exists `t_user` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(255) DEFAULT NULL,\n" +
//                "  `password` varchar(255) DEFAULT NULL,\n" +
//                "  `company` varchar(255) DEFAULT NULL,\n" +
//                "  `sex` int(255) DEFAULT NULL,\n" +
//                " `tenant_id` int(11) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`)\n" +
//                ")";
//        map.put("sql",createTable);
//        this.serverInfoMapper.executeCreateQuery(map);
//
//        for(int i=0;i<8;i++){
//            String createDiffTable="CREATE TABLE If Not Exists `QDFinance_"+i+"` (\n" +
//                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                    "  `fromId` int(11) unsigned NOT NULL COMMENT '使用用户的id',\n" +
//                    "  `content` varchar(4096) COLLATE utf8mb4_bin DEFAULT '' COMMENT '数据内容',\n" +
//                    "  `type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '数据类型',\n" +
//                    "  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0正常 1被删除',\n" +
//                    "  `updated` int(11) unsigned NOT NULL COMMENT '更新时间',\n" +
//                    "  `created` int(11) unsigned NOT NULL COMMENT '创建时间',\n" +
//                    "  PRIMARY KEY (`id`)\n" +
//                    ") ENGINE=InnoDB AUTO_INCREMENT=2108171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin" ;
//            map.put("sql",createDiffTable);
//            this.serverInfoMapper.executeCreateQuery(map);
//        }
//
//
//        User user=new User();
//        user.setName(tenant.getTenantAccount());
//        user.setPassword(tenant.getTenantPassword());
//        user.setCompanyName(tenant.getCompanyName());
//        user.setSex(1);
//        user.setTenantId(tenant.getId());
//        this.userMapper.insertUser(user);
//
//        resultMap.put("tenant",tenant);
//
//        resultMap.put("msg","create userdb successlly");
//
//        return resultMap;
//    }

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
            /*user1.setDbName(user.getDbName());*/
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
    public Map getUserList(User user) {
        Map<String,Object> resultMap=new HashMap<>();
        /*user.setSearchText("%"+user.getSearchText()+"%");*/
       List<User> users=this.userMapper.getList(user);
        if(users!=null && users.size()>0){
            resultMap.put("users",users);
            resultMap.put("msg","get user list successlly");
        }else{
            resultMap.put("users",new ArrayList<User>());
            resultMap.put("msg","get user list fail");
        }
        return resultMap;
    }

    @Override
    public Map getAllTenant() {
        Map<String,Object> resultMap=new HashMap<>();
        List<TenantInfo> users=this.serverInfoMapper.getAllUser();
        if(users!=null && users.size()>0){
            resultMap.put("tenantInfo",users);
            resultMap.put("msg","get tenantInfo list successlly");
        }else{
            resultMap.put("tenantInfo",new ArrayList<TenantInfo>());
            resultMap.put("msg","get tenantInfo list fail");
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
    public Map insertUserDifferentTable(User user) {
        String tableIndex=getRoundingTableIndex(user.getTenantId());
        /*user.setTableIndex(tableIndex);*/
       String sql="Create Table If Not Exists  `t_user"+tableIndex+"` ("  +
        "`id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) DEFAULT NULL,\n" +
                "  `password` varchar(255) DEFAULT NULL,\n" +
                "  `company` varchar(255) DEFAULT NULL,\n" +
                "  `sex` int(255) DEFAULT NULL,\n" +
               "`tenant_id` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ")";
        Map<String,Object> map=new HashMap<>();
        map.put("sql",sql);
        this.userMapper.executeCreateQuery(map);
        this.userMapper.insertUserByTableIndex(user);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("msg","save user in different table successfully");
        return resultMap;
    }
    @Override
    public Map queryUserDifferentTable(User user) {
        String tableIndex=getRoundingTableIndex(user.getTenantId());
        /*user.setSearchText( "%"+user.getSearchText()+"%");*/
        /*user.setTableIndex(tableIndex);*/
        List<User> users=this.userMapper.getUserByTableIndex(user);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("msg","query user in different table successfully");
        resultMap.put("users",users);
        return resultMap;
    }



    @Override
    public Map insertFinanceDifferentTable(Finance finance) {
        String tableIndex=getRemainderTableIndex(finance.getFromId());
        finance.setTableIndex(tableIndex);
        this.financeMapper.insertFinanceByTableIndex(finance);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("msg","save finance in different table successfully");
        return resultMap;
    }
    @Override
    public Map queryFinanceDifferentTable(Finance finance) {
        String tableIndex=getRemainderTableIndex(finance.getFromId());
        finance.setSearchText( "%"+finance.getSearchText()+"%");
        finance.setTableIndex(tableIndex);
        List<Finance> finances=this.financeMapper.getFinanceByTableIndex(finance);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("msg","query finances in different table successfully");
        resultMap.put("finances",finances);
        return resultMap;
    }



    public String getRoundingTableIndex(Integer id){
       return String.format("%04d", id/30);
    }

    public String getRemainderTableIndex(Integer id){
        return ""+id%8;
    }


    @Override
    public void createDB()
    {
        AdminUser user = new AdminUser();
        user.setUsername("liyouTenant");
        user.setPassword("liyouTenant");
        user.setPhoneNo("18520802618");
        userDbCreator.createTenantDb(user);
    }

    public void addDB(DataBaseInfo dataBaseInfo){
/*        DynamicDataSource dynamicDataSource= SpringContextUtil.getBean("multipleDataSource");
        dynamicDataSource.addDataSource(dataBaseInfo);*/
    }
    //上传
         public String uploadPic(byte[] pic, String name, long size){
                return FastDFSUtils.uploadPic(pic, name, size);
         }
        public String uploadPic(String picPath, String name, long size){
            return FastDFSUtils.uploadPic(picPath, name, size);
        }
}
