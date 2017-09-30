package com.lesso.service;

import com.lesso.common.db.DataBaseInfo;
import com.lesso.pojo.*;

import java.util.Map;

/**
 * Created by czx on 2017/9/26.
 */
public interface IAccountService {
    public TenantInfo getTenantInfo(String tenantAccount);
    public Map createUserInfo(TenantInfo tenant);
    public Map createUserDB(AdminUser adminUser,User user);
    public Map loginOfTenantInfo(TenantInfo tenant);
    public Map loginOfUser(User user);
    public Map getVerificationCode(Msg msg);

}
