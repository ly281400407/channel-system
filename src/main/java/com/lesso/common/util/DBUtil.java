package com.lesso.common.util;

import com.ibatis.common.jdbc.ScriptRunner;
import com.lesso.common.db.ScriptGenerator;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库工具
 * 主要用于新商户入驻 建库 建表用
 */
public class DBUtil {

    private static Logger logger = Logger.getLogger(DBUtil.class);

    private static ScriptGenerator scriptGenerator = new ScriptGenerator();

    public static void recoveryTenant(String ip, int port, String connectUser, String connectPassword, String user, String dbName){

        try {

            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("user", user);
            parameter.put("dbName", dbName);

            Connection conn = getConnection(ip, port, connectUser, connectPassword, "mysql");
            File file = scriptGenerator.generatorScript(parameter, "template/recovery-tenant-database.sql");
            runScript(file, conn);
            file.delete();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

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
    public static synchronized void createDataBase(String ip, int port, String connectUser, String connectPassword, String dbName, String user, String userPass) throws IOException, SQLException {

        try {
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("dbName", dbName);
            parameter.put("username", user);
            parameter.put("password", userPass);
            Connection conn = getConnection(ip, port, connectUser, connectPassword, "mysql");
            File file = scriptGenerator.generatorScript(parameter, "template/create-tenant-database.sql");
            runScript(file, conn);
            file.delete();
        }catch (IOException e){
            logger.error(e.getMessage());
            throw e;
        }



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
    public static synchronized void createTenantTable(String ip, int port, String connectUser, String connectPassword, String dbName) throws IOException, SQLException {

        try {
            Connection conn = getConnection(ip, port, connectUser, connectPassword, dbName);
            File file = scriptGenerator.generatorScript(null, "template/create-tenant-table.sql");
            runScript(file, conn);
            file.delete();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw e;
        }
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

    private static void runScript(File file, Connection conn) throws IOException, SQLException {

        try {
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            runner.runScript(inputStreamReader);
        }catch (Exception e ){
            logger.error(e.getMessage());
            throw e;
        }
    }


}
