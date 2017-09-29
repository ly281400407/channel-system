package com.lesso.common.db;

import com.lesso.common.util.StringUtil;
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
        String dbName = determineCurrentLookupKey();

        if(StringUtil.isEmpty(dbName)){
            return masterDataSource;
        }

        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource = (DataSource) targetDataSource.get(dbName);

        //如果没有对应数据源 则创建一个对应的数据源
        if(null == dataSource){
            dataSource = createDataSource(dbName);
        }

        if(dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup daName [" + dbName + "]");
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
        if(null!= dataSource) {
            setDataSource(dbName, dataSource);
        }
        return dataSource;
    }

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
//       if(this.targetDataSources != null) {
//            Iterator var1 = this.targetDataSources.entrySet().iterator();
//            while(var1.hasNext()) {
//                Map.Entry entry = (Map.Entry)var1.next();
//                Object lookupKey = this.resolveSpecifiedLookupKey(entry.getKey());
//                DataSource dataSource = this.resolveSpecifiedDataSource(entry.getValue());
//                this.targetDataSource.put(lookupKey, dataSource);
//            }
//        }
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

}
