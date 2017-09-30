package com.lesso.common.util;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import com.lesso.common.db.ScriptGenerator;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class DBUtil {

    private static Logger logger = Logger.getLogger(DBUtil.class);

    private static ScriptGenerator scriptGenerator = new ScriptGenerator();

    public static synchronized boolean createDataBase(String ip, int port, String connectUser, String connectPassword, String dbName, String user, String userPass){

        try {

            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("dbName", dbName);
            parameter.put("username", user);
            parameter.put("password", userPass);

            File file = scriptGenerator.generatorScript(parameter);
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            String url = "jdbc:mysql://" + ip + ":" + port;
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver).newInstance();
            Connection conn = (Connection) DriverManager.getConnection(url, connectUser, connectPassword);
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            runner.runScript(inputStreamReader);
        }catch (Exception e ){
            logger.error(e.getMessage());
            return false;
        }
        return true;

    }


}
