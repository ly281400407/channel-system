package com.lesso.common.listener;

import com.lesso.common.util.SpringContextUtil;
import com.lesso.common.db.DataBaseInfo;
import com.lesso.common.db.DataSourceHolder;
import com.lesso.common.db.DynamicDataSource;
import com.lesso.mapper.manager.ServerInfoMapper;
import com.lesso.pojo.TenantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by czx on 2017/9/22.
 */
@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ServerInfoMapper serverInfoMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

/*      List<TenantInfo> tenantInfoList= serverInfoMapper.getAllUser();
        for(TenantInfo tenant:tenantInfoList){
            DataBaseInfo dataBaseInfo=new DataBaseInfo();
            dataBaseInfo.setDbKey(tenant.getDbName());
            dataBaseInfo.setUrl("jdbc:mysql://"+tenant.getServerIp()+":"+tenant.getServerPort()+"/"+tenant.getDbName()+"?serverTimezone=GMT&autoReconnect=true");
            dataBaseInfo.setUserName(tenant.getDbAccount());
            dataBaseInfo.setPassword(tenant.getDbPassword());
            DynamicDataSource dynamicDataSource= SpringContextUtil.getBean("multipleDataSource");
            dynamicDataSource.addDataSource(dataBaseInfo);
            DataSourceHolder.setDataSource(tenant.getDbName());
        }*/
//        Map<String,String> map=new HashMap<>();
//        map.put("139.199.62.80","tenantDataSource1");
//        map.put("118.89.64.133","tenantDataSource2");
//        ServerUtil.setMap(map);
    }
}
