package com.lesso.common.db;

import com.lesso.common.util.DBUtil;
import com.lesso.pojo.ServerInfo;

public class DefaultUserDbCreator implements AbstractUserDbCreator{

    private AbstractServerDistributor serverDistributor;

    public AbstractServerDistributor getServerDistributor() {
        return serverDistributor;
    }

    public void setServerDistributor(AbstractServerDistributor serverDistributor) {
        this.serverDistributor = serverDistributor;
    }

    @Override
    public void createUserDb(Object o) {

        String tenantAccount = (String) o;
        String dbName = tenantAccount;
        String dbPassword = tenantAccount;
        String dbAccount = tenantAccount;
        ServerInfo serverInfo = serverDistributor.distribution(o);

        DBUtil dBUtil = new DBUtil();
        dBUtil.createDataBase(serverInfo.getServerIp(), serverInfo.getServertPort(), serverInfo.getManagerUser(), serverInfo.getManagerPassword(), dbName, dbAccount, dbPassword);
    }

}
