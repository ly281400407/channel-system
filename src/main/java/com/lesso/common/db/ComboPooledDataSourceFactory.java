package com.lesso.common.db;

import com.lesso.common.util.StringUtil;
import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.pojo.TenantInfo;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComboPooledDataSourceFactory implements AbstractDataSourceFactory{

    @Autowired
    JdbcTemplate jdbcTemplate;

    //jdbcUrl前缀 拼接jdbcUrl需要用到
    private String jdbcUrlPrefix;

    //jdbcUrl前缀 拼接jdbcUrl需要用到
    private String jdbcUrlSuffix;

    private String driverClassName;

    //初始化池大小
    private int initialPoolSize;

    //连接池保持的最小连接数
    private int minPoolSize;

    //连接池在无空闲连接可用时一次性创建的新数据库连接数,default:3
    private int acquireIncrement;

    //连接池中拥有的最大连接数，如果获得新连接时会使连接总数超过这个值则不会再获取新连接，
    // 而是等待其他连接释放，所以这个值有可能会设计地很大,default : 15
    private int maxPoolSize;

    //连接的最大空闲时间，如果超过这个时间，某个数据库连接还没有被使用，则会断开掉这个连接,单位秒
    private int maxIdleTime;

    //连接池在获得新连接失败时重试的次数，如果小于等于0则无限重试直至连接获得成功
    private int acquireRetryAttempts;

    //连接池在获得新连接时的间隔时间
    private int acquireRetryDelay;

    public void setJdbcUrlPrefix(String jdbcUrlPrefix) {
        this.jdbcUrlPrefix = jdbcUrlPrefix;
    }

    public void setJdbcUrlSuffix(String jdbcUrlSuffix) {
        this.jdbcUrlSuffix = jdbcUrlSuffix;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public void setAcquireIncrement(int acquireIncrement) {
        this.acquireIncrement = acquireIncrement;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public void setAcquireRetryAttempts(int acquireRetryAttempts) {
        this.acquireRetryAttempts = acquireRetryAttempts;
    }

    public void setAcquireRetryDelay(int acquireRetryDelay) {
        this.acquireRetryDelay = acquireRetryDelay;
    }

    @Override
    public DataSource createDataSource(Object o) {

        ComboPooledDataSource comboPooledDataSource = null;

        try {

            final String dbName = (String) o;
            //TenantInfo tenantInfo = tenantInfoMapper.getTenantInfo(tenantAccount);
            TenantInfo tenantInfo =  jdbcTemplate.queryForObject("select * from QDTenantInfo where dbName = '" + dbName + "' limit 1", new RowMapper<TenantInfo>() {
                @Override
                public TenantInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                    TenantInfo tenantInfo = new TenantInfo();
                    tenantInfo.setDbPassword(resultSet.getString("dbPassword"));
                    tenantInfo.setDbName(resultSet.getString("dbName"));
                    tenantInfo.setDbAccount(resultSet.getString("db_Account"));
                    tenantInfo.setServerIp(resultSet.getString("serverIp"));
                    tenantInfo.setServerPort(resultSet.getInt("servertPort"));
                    return tenantInfo;
                }
            });

            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setPassword(tenantInfo.getDbPassword());
            comboPooledDataSource.setUser(tenantInfo.getDbAccount());
            comboPooledDataSource.setDriverClass(driverClassName);
            comboPooledDataSource.setInitialPoolSize(initialPoolSize);
            comboPooledDataSource.setMinPoolSize(minPoolSize);
            comboPooledDataSource.setAcquireIncrement(acquireIncrement);
            comboPooledDataSource.setMaxPoolSize(maxPoolSize);
            comboPooledDataSource.setMaxIdleTime(maxIdleTime);
            comboPooledDataSource.setAcquireRetryAttempts(acquireRetryAttempts);
            comboPooledDataSource.setAcquireRetryDelay(acquireRetryDelay);
            String jdbcUrl = this.createJdbcUrl(tenantInfo);
            comboPooledDataSource.setJdbcUrl(jdbcUrl);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        return comboPooledDataSource;
    }

    /**
     * 拼接jdbcUrl
     * @param tenantInfo
     * @return
     */
    private String createJdbcUrl(TenantInfo tenantInfo){

        String ip = tenantInfo.getServerIp();
        int port = tenantInfo.getServerPort();
        String dbName = tenantInfo.getDbName();

        StringBuilder jdbcUrl = new StringBuilder("");
        jdbcUrl.append(jdbcUrlPrefix);
        jdbcUrl.append(ip);
        jdbcUrl.append(":");
        jdbcUrl.append(port);
        jdbcUrl.append("/");
        jdbcUrl.append(dbName);
        if(!StringUtil.isEmpty(jdbcUrlSuffix)){
            jdbcUrl.append(jdbcUrlSuffix);
        }

        return jdbcUrl.toString();
    }
}
