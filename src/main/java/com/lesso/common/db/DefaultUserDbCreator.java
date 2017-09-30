package com.lesso.common.db;

import com.lesso.common.util.DBUtil;
import com.lesso.pojo.AdminUser;
import com.lesso.pojo.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultUserDbCreator implements AbstractUserDbCreator{

    @Autowired
    JdbcTemplate jdbcTemplate;

    private AbstractServerDistributor serverDistributor;

    public AbstractServerDistributor getServerDistributor() {
        return serverDistributor;
    }

    public void setServerDistributor(AbstractServerDistributor serverDistributor) {
        this.serverDistributor = serverDistributor;
    }

    @Override
    public void createTenantDb(Object o) {

        AdminUser user = (AdminUser) o;
        ServerInfo serverInfo = serverDistributor.distribution(user);

        //创库
        DBUtil.createDataBase(serverInfo.getServerIp(), serverInfo.getServerPort(), serverInfo.getManagerUser(), serverInfo.getManagerPassword(), user.getUsername(), user.getUsername(), user.getPassword());
        //创表
        DBUtil.createTenantTable(serverInfo.getServerIp(), serverInfo.getServerPort(), user.getUsername(), user.getPassword(), user.getUsername());

        //创建tenant租户信息
        StringBuilder sb = new StringBuilder("insert into QDTenantInfo(tenantAccount, tenantPassword, serverIp, serverPort, dbName, dbPassword, status, created, updated, phoneNo, dbAccount)");
        sb.append("values('"+user.getUsername()+"','"+user.getPassword()+"','"+serverInfo.getServerIp()+"','"+serverInfo.getServerPort()+"','"+user.getUsername()+"','"+user.getPassword()+"','"+"1"+"',sysdate(),sysdate(),'"+user.getPhoneNo()+"','"+user.getPassword()+"');");
        jdbcTemplate.execute(sb.toString());

        //更新服务器信息
        String updateSql = "update QDServerInfo si set si.tenantCurcount = si.tenantCurcount + 1 where si.serverIp = '"+serverInfo.getServerIp()+"' and si.serverPort = '"+serverInfo.getServerPort()+"'";
        jdbcTemplate.execute(updateSql);
    }

}
