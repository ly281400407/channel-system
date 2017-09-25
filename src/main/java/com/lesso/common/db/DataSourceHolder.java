package com.lesso.common.db;

/**
 * Created by czx on 2017/9/20.
 */
public class DataSourceHolder {

//    private static String defaultDataSourceName = "dbmanager";

    //线程本地环境
    private static final ThreadLocal<String> dataSourceName = new ThreadLocal<String>();

    //设置数据源
    public static void setDataSource(String tenantAccount) {
        dataSourceName.set(tenantAccount);
    }

    //获取数据源
    public static String getDataSource() {
/*        if(null==dataSourceName.get()){
            dataSourceName.set(defaultDataSourceName);
        }*/
        return dataSourceName.get();
    }

    //清除数据源
    public static void clearDataSource() {
        dataSourceName.remove();
    }

}
