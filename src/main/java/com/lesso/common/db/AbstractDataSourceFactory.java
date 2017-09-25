package com.lesso.common.db;

import javax.sql.DataSource;

public interface AbstractDataSourceFactory {

    public DataSource createDataSource(Object o);

}
