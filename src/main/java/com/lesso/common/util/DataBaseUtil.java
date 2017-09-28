package com.lesso.common.util;

/**
 * Created by czx on 2017/9/28.
 */
public class DataBaseUtil {

    public static String getRootOfBusinessDB(String ip){
        if("118.89.64.133".equals(ip)){
           return "tenantDataSource2";
        }else{
           return "tenantDataSource1";
        }
    }
}
