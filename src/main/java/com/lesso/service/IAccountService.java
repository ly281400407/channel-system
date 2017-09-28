package com.lesso.service;

import com.lesso.common.db.DataBaseInfo;
import com.lesso.pojo.Finance;
import com.lesso.pojo.Msg;
import com.lesso.pojo.TenantInfo;
import com.lesso.pojo.User;

import java.util.Map;

/**
 * Created by czx on 2017/9/26.
 */
public interface IAccountService {
    public TenantInfo getTenantInfo(String tenantAccount);
    public Map createUserInfo(TenantInfo tenant);
    public Map createUserDB(TenantInfo tenant);
    public Map loginOfTenantInfo(TenantInfo tenant);
    public Map loginOfUser(User user);
    public Map addUser(User user);
    public Map getMsg(Msg msg);

}
