package com.lesso.common.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataSourceFactory {

    public Map<String, String> urlProperties = new HashMap<String, String>();

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


    public void setUrlProperties(Map<String, String> urlProperties) {
        this.urlProperties = urlProperties;
    }

    public void setDirver(String dirver) {
        this.dirver = dirver;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public synchronized DataSource createDataSource(){

        DataSource basicDataSource = new ComboPooledDataSource();
/*        basicDataSource.setDriverClassName(dirver);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setInitialSize(initialSize);
        basicDataSource.setMaxActive(maxActive);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxWait(maxWait);*/

/*        String url = createUrl();
        basicDataSource.setUrl(url);*/

        return basicDataSource;
    }

    private String createUrl(){

        /*jdbc:mysql://139.199.62.80:2208/qudao?serverTimezone=GMT*/
        StringBuilder urlSb = new StringBuilder("");
        urlSb.append("jdbc:mysql://");
        urlSb.append(ip);
        urlSb.append(":");
        urlSb.append(port);
        urlSb.append("/");
        urlSb.append(dbName);
        //urlSb.append("?serverTimezone=GMT");
        if(urlProperties.size()>0){
            urlSb.append("?");
            Iterator<String> it = urlProperties.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                String value = urlProperties.get(key);
                urlSb.append(key);
                urlSb.append("=");
                urlSb.append(value);
                urlSb.append("&");
            }
            urlSb.deleteCharAt(urlSb.lastIndexOf("&"));
        }
        return urlSb.toString();
    }

}
