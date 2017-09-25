package com.lesso.common.util;

import java.util.Map;

/**
 * Created by czx on 2017/9/23.
 */
public class ServerUtil {
    private static Map<String,String> map;

    public static Map<String, String> getMap() {
        return map;
    }

    public static void setMap(Map<String, String> map) {
        ServerUtil.map = map;
    }

    public static String  getDataName(String ip){
      return map.get(ip);
    }
}
