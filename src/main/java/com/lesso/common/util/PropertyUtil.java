package com.lesso.common.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by czx on 2017/9/28.
 */
public class PropertyUtil {

    public static  Properties loadProps(String propertiesName){
        Properties  props = new Properties();
        InputStream in = null;
        try {
           //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            props.load(in);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {

            }
        }
        return props;
    }

    public static String getProperty(String propertiesName,String key){
        Properties props =loadProps(propertiesName);
        return props.getProperty(key);
    }

    public static void main(String[] args){
       System.out.print(PropertyUtil.getProperty("config.properties","sms.serverUrl"));
    }


}
