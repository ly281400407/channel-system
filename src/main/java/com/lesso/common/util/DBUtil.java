package com.lesso.common.util;

import com.ibatis.common.jdbc.ScriptRunner;
import com.lesso.common.db.ScriptGenerator;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库工具
 * 主要用于新商户入驻 建库 建表用
 */
public class DBUtil {

    private static Logger logger = Logger.getLogger(DBUtil.class);

    private static ScriptGenerator scriptGenerator = new ScriptGenerator();

    /**
     * 创建新库
     * @param ip
     * @param port
     * @param connectUser
     * @param connectPassword
     * @param dbName
     * @param user
     * @param userPass
     * @return
     */
    public static synchronized boolean createDataBase(String ip, int port, String connectUser, String connectPassword, String dbName, String user, String userPass){

        try {
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("dbName", dbName);
            parameter.put("username", user);
            parameter.put("password", userPass);
            Connection conn = getConnection(ip, port, connectUser, connectPassword, "mysql");
            File file = scriptGenerator.generatorScript(parameter, "template/create-tenant-database.sql");
            runScript(file, conn);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;

    }

    /**
     * 创建商户表
     * @param ip
     * @param port
     * @param connectUser
     * @param connectPassword
     * @param dbName
     * @return
     */
    public static synchronized boolean createTenantTable(String ip, int port, String connectUser, String connectPassword, String dbName){

        try {
            Connection conn = getConnection(ip, port, connectUser, connectPassword, dbName);
            File file = scriptGenerator.generatorScript(null, "template/create-tenant-table.sql");
            runScript(file, conn);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 添加商户数据
     * @param ip
     * @param port
     * @param connectUser
     * @param connectPassword
     * @param dbName
     * @return
     */
    public static synchronized boolean createTenantData(String ip, int port, String connectUser, String connectPassword, String dbName){
        return true;
    }

    private static Connection getConnection(String ip, int port, String user, String password, String dbName){

        try {
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver).newInstance();
            Connection conn = (Connection) DriverManager.getConnection(url, user, password);
            return conn;
        }catch (Exception e){
            logger.info(e.getMessage());
            return null;
        }


    }

    private static boolean runScript(File file, Connection conn){
        try {
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            runner.runScript(inputStreamReader);
        }catch (Exception e ){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }


}
