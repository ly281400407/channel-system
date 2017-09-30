package com.lesso.common.db;

import com.lesso.pojo.AdminUser;
import com.lesso.pojo.ServerInfo;

/**
 * 默认的数据库服务分配器
 */
public class DefaultServerDistributor implements AbstractServerDistributor{

    @Override
    public ServerInfo distribution(Object o) {
        AdminUser adminUser = (AdminUser)o;
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerIp("139.199.62.80");
        serverInfo.setServerPort(2208);
        serverInfo.setManagerUser("root");
        serverInfo.setManagerPassword("lesso@2128");
        return serverInfo;
    }

}
