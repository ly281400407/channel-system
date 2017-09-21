package com.lesso.common.data;

import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.pojo.TenantInfo;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

public class SqlSessionPool {

    private DataSourceFactory dataSourceFactory;

    private Map<String, SqlSession> sqlSessionMap = new HashMap<String, SqlSession>();

    private SqlSession rootSqlSession;

    private Resource[] mapperLocations;

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public void setRootSqlSessionFactory(SqlSessionFactory rootSqlSessionFactory) {
        this.rootSqlSession = rootSqlSessionFactory.openSession();
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public SqlSession getSqlSession(String id) throws Exception {
        SqlSession sqlSession = sqlSessionMap.get(id);
        if(null == sqlSession){
            TenantInfoMapper tenantInfoMapper = rootSqlSession.getMapper(TenantInfoMapper.class);
            TenantInfo tenantInfo = tenantInfoMapper.getTenantInfo(id);
            if(null == tenantInfo){
                throw new Exception();
            }else {
                sqlSession = createSqlSession(id);
                sqlSessionMap.put(id, sqlSession);
            }
        }
        return sqlSession;
    }

    private SqlSession createSqlSession(String id) throws Exception {
        TenantInfoMapper tenantInfoMapper =rootSqlSession.getMapper(TenantInfoMapper.class);
        TenantInfo tenantInfo = tenantInfoMapper.getTenantInfo(id);

        dataSourceFactory.username = tenantInfo.dbName;
        dataSourceFactory.password = tenantInfo.dbPassword;
        dataSourceFactory.ip = tenantInfo.serverIp;
        dataSourceFactory.port = tenantInfo.servertPort;
        dataSourceFactory.dbName = tenantInfo.dbName;
        BasicDataSource dataSource = (BasicDataSource) dataSourceFactory.createDataSource();

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();

        return sqlSessionFactory.openSession();
    }

}
