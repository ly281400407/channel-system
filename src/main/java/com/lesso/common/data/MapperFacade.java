package com.lesso.common.data;

import org.apache.ibatis.session.SqlSession;

public class MapperFacade implements AbstractMapperFacade{

    private SqlSessionPool sqlSessionPool;

    public SqlSessionPool getSqlSessionPool() {
        return sqlSessionPool;
    }

    public void setSqlSessionPool(SqlSessionPool sqlSessionPool) {
        this.sqlSessionPool = sqlSessionPool;
    }

    public <T> T getMapper(String id, Class<T> var1) throws Exception {

        SqlSession sqlSession = sqlSessionPool.getSqlSession(id);
        return sqlSession.getMapper(var1);

    }
}
