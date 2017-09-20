package com.lesso.common.data;

import com.lesso.mapper.manager.TenantInfoMapper;
import com.lesso.pojo.TenantInfo;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;

import java.util.Map;

public class SqlSessionPool {

    DataSourceFactory dataSourceFactory;

    Map<String, SqlSession> sqlSessionMap;

    SqlSession rootSqlSession;

    SqlSessionFactoryBean rootSqlSessionFactoryBean;

    String mapperPath;


    public SqlSession getSqlSession(String id){
        SqlSession sqlSession = sqlSessionMap.get(id);
        return sqlSession;
    }

    private SqlSession createSqlSession(String id){
        TenantInfoMapper tenantInfoMapper =rootSqlSession.getMapper(TenantInfoMapper.class);
        TenantInfo tenantInfo = tenantInfoMapper.getTenantInfo(id);

        dataSourceFactory.username = tenantInfo.dbName;
        dataSourceFactory.password = tenantInfo.dbPassword;

        return null;
    }

}
