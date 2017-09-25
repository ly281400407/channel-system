package com.lesso.mapper.manager;

import com.lesso.pojo.ServerInfo;
import com.lesso.pojo.TenantInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by czx on 2017/9/20.
 */
public interface ServerInfoMapper {
    public ServerInfo get(Integer id);

    public int executeCreateQuery(Map<String, Object> map);

    public List<ServerInfo> getRegisterServerInfo();

    public TenantInfo getUserDBInfo(String companyName);

    public int insertUser(TenantInfo tenantInfo);

    public int updateQDServerInfo(String ip);

    public TenantInfo getUserInfo(TenantInfo tenantInfo);

    public List<TenantInfo> getAllUser();

}
