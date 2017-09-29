package com.lesso.common.util;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private Logger logger = Logger.getLogger(DBUtil.class);

    public synchronized boolean createDataBase(String ip, int port, String connectUser, String connectPassword, String dbName, String user, String userPass){

        try {
            String url = "jdbc:mysql://" + ip + ":" + port;
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver).newInstance();
            Connection conn = (Connection) DriverManager.getConnection(url, connectUser, connectPassword);
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            runner.runScript(Resources.getResourceAsReader("jpetstore-mysql-schema.sql"));
        }catch (Exception e ){
            logger.error(e.getMessage());
            return false;
        }
        return true;

    }


}
