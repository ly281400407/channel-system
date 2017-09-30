package com.lesso.mapper.manager;

import com.lesso.pojo.AdminUser;
import com.lesso.pojo.ServerInfo;
import com.lesso.pojo.TenantInfo;
import com.lesso.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * Created by czx on 2017/9/20.
 */
public interface ServerInfoMapper {
    public ServerInfo get(Integer id);

    public int executeCreateQuery(Map<String, Object> map);

    public List<ServerInfo> getRegisterServerInfo();

    public TenantInfo getUserDBInfo(String phoneNo);

    public int insertTenantInfo(TenantInfo tenantInfo);

    public int insertUser(AdminUser user);

    public int updateAdminUserStatus(AdminUser user);

    public int updateQDServerInfo(String ip);

    public TenantInfo getUserInfo(TenantInfo tenantInfo);

    public List<TenantInfo> getAllUser();

}
