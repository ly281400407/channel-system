package com.lesso.common.db;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by czx on 2017/9/20.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Logger log = Logger.getLogger(DynamicDataSource.class);

    // 默认数据源，也就是主库
    protected DataSource masterDataSource;

    //分库
    protected Map<Object, Object> targetDataSources;

    //数据源创建工厂
    private AbstractDataSourceFactory dataSourceFactory;

    // 保存动态创建的数据源
    private static final Map targetDataSource = new HashMap<>();

    @Override
    protected DataSource determineTargetDataSource() {
        // 根据数据库选择方案，拿到要访问的数据库
        String daName = determineCurrentLookupKey();

        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource = (DataSource) targetDataSource.get(daName);

        //如果没有对应数据源 则创建一个对应的数据源
        if(null == dataSource){
            dataSource = createDataSource(daName);
        }
        if(dataSource == null || daName == null) {
            dataSource = this.masterDataSource;
        }

        if(dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup daName [" + daName + "]");
        } else {
            return dataSource;
        }
    }

    @Override
    protected String determineCurrentLookupKey() {
        String dataSourceName = DataSourceHolder.getDataSource();
        return dataSourceName;
    }

    public void addTargetDataSource(String key, DataSource dataSource) {
        this.targetDataSource.put(key, dataSource);
    }
    public void removeTargetDataSource(String key) {
        this.targetDataSource.remove(key);
    }

    public void setDataSourceFactory(AbstractDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    /**
     * 创建数据源
     * @param dbName
     * @return
     */
    private DataSource createDataSource(String dbName){
        DataSource dataSource = dataSourceFactory.createDataSource(dbName);
        return dataSource;
    }

/*
    *//**
     * 该方法为同步方法，防止并发创建两个相同的数据库
     * 使用双检锁的方式，防止并发
     * @param dbType
     * @return
     *//*
    private synchronized DataSource selectDataSource(String dbType){
        // 再次从数据库中获取，双检锁
        DataSource obj = (DataSource)this.targetDataSource.get(dbType);
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据库
        DataSource dataSource = this.getDataSource(dbType);
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.setDataSource(dbType, dataSource);
            return dataSource;
        }else {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + dbType + "]");
        }
    }

    *//**
     * 从主数据库查询对应数据库的信息
     * @param dbtype
     * @return
     *//*
    private DataSource getDataSource(String dbtype) {
        String oriType = DataSourceHolder.getDataSource();
        // 先切换回主库
        DataSourceHolder.setDataSource("dataSource");
        // 查询所需信息
*//*        DataBaseInfo datebase = centerDatebaseManager.getById(dbtype);*//*
        DataBaseInfo datebase=new DataBaseInfo();
        // 切换回目标库
        DataSourceHolder.setDataSource(oriType);

        DataSource dataSource = createDataSource(datebase.getUrl(),datebase.getUserName(),datebase.getPassword());
        return dataSource;
    }


    //创建SQLServer数据源
    private DataSource createDataSource(String url, String userName, String password) {
        return createDataSource("com.mysql.jdbc.Driver", url, userName, password);
    }

    //创建数据源
    private DataSource createDataSource(String driverClassName, String jdbcUrl,
                                             String username, String password){
        ComboPooledDataSource dataSource = null;
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driverClassName);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(username);
            dataSource.setPassword(password);
        } catch (PropertyVetoException e) {
            log.error(e.getMessage());
        }
        return dataSource;
    }
    public synchronized DataSource addDataSource(DataBaseInfo dataBaseInfo){
        DataSource obj = (DataSource)this.targetDataSource.get(dataBaseInfo.getDbKey());
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据库
        DataSource dataSource = createDataSource(dataBaseInfo.getUrl(),dataBaseInfo.getUserName(),dataBaseInfo.getPassword());
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.addTargetDataSource(dataBaseInfo.getDbKey(),dataSource);
            return dataSource;
        }else {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + dataBaseInfo.getDbKey() + "]");
        }
    }*/

    public synchronized void removeDataSource(DataBaseInfo dataBaseInfo){
      this.removeTargetDataSource(dataBaseInfo.getDbKey());
    }


    public void changDataSource(String dbKey){
        DataSourceHolder.setDataSource(dbKey);
    }

    public void setDataSource(String type, DataSource dataSource) {
        this.addTargetDataSource(type, dataSource);
        DataSourceHolder.setDataSource(type);
    }



    /**
     * 该方法重写为空，因为AbstractRoutingDataSource类中会通过此方法将，targetDataSources变量中保存的数据源交给resolvedDefaultDataSource变量
     * 在本方案中动态创建的数据源保存在了本类的targetDataSource变量中。如果不重写该方法为空，会因为targetDataSources变量为空报错
     * 如果仍然想要使用AbstractRoutingDataSource类中的变量保存数据源，则需要在每次数据源变更时，调用此方法来为resolvedDefaultDataSource变量更新
     */
    @Override
    public void afterPropertiesSet() {
        if(this.targetDataSources != null) {
            Iterator var1 = this.targetDataSources.entrySet().iterator();
            while(var1.hasNext()) {
                Map.Entry entry = (Map.Entry)var1.next();
                Object lookupKey = this.resolveSpecifiedLookupKey(entry.getKey());
                DataSource dataSource = this.resolveSpecifiedDataSource(entry.getValue());
                this.targetDataSource.put(lookupKey, dataSource);
            }
        }
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

}
