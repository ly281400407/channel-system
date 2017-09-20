package com.lesso.common.data;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataSourceFactory {

    public Map<String, String> urlproperties = new HashMap<String, String>();

    public String dirver;

    public String ip;

    public String port;

    public String dbName;

    public String username;

    public String password;

    public int initialSize;

    public int maxActive;

    public int maxIdle;

    public int minIdle;

    public int maxWait;

    public DataSource createDataSource(){

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(dirver);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(initialSize);
        basicDataSource.setMaxActive(maxActive);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxWait(maxWait);

        String url = createUrl();
        basicDataSource.setUrl(url);

        return basicDataSource;
    }

    private String createUrl(){

        StringBuilder urlSb = new StringBuilder("");
        urlSb.append("http://");
        urlSb.append(ip);
        urlSb.append(":");
        urlSb.append(port);
        urlSb.append("/");
        urlSb.append(dbName);
        if(urlproperties.size()>0){

            urlSb.append("?");
            Iterator<String> it = urlproperties.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                String value = urlproperties.get(key);
                urlSb.append(key);
                urlSb.append("=");
                urlSb.append(value);
                urlSb.append("&");
            }
            urlSb.deleteCharAt(urlSb.lastIndexOf("&"));
        }
        return null;
    }

}
