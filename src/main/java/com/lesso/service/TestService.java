package com.lesso.service;

import com.lesso.common.db.DataBaseInfo;
import com.lesso.pojo.Finance;
import com.lesso.pojo.TenantInfo;
import com.lesso.pojo.User;

import java.util.Map;

public interface TestService {
    public void createDB();
    public void addDB(DataBaseInfo dataBaseInfo);
    public User test();
    public TenantInfo getTenantInfo(String tenantAccount);
    public Map createUserInfo(TenantInfo tenant);
    public Map createUserDB(TenantInfo tenant);
    public Map loginOfTenantInfo(TenantInfo tenant);
    public Map loginOfUser(User user);
    public Map getUserList(User user);
    public Map getAllTenant();
    public Map addUser(User user);
    public Map insertUserDifferentTable(User user);
    public Map queryUserDifferentTable(User user);
    public Map insertFinanceDifferentTable(Finance finance);
    public Map queryFinanceDifferentTable(Finance finance);
    public String uploadPic(byte[] pic, String name, long size);
    public String uploadPic(String picPath, String name, long size);

}
