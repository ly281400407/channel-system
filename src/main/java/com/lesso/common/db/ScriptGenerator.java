package com.lesso.common.db;

import java.io.*;
import java.util.Map;

/**
 * 脚本生成器
 * 生成用户建库脚本
 */
public class ScriptGenerator {

    public File generatorScript(Map<String, String> parameter){

        String dbName = (String) parameter.get("dbName");
        String userName = (String) parameter.get("userName");
        String password = (String) parameter.get("password");

        long systime = System.currentTimeMillis();
        String fileName = systime+".sql";
        File file = new File(fileName);

        File template = new File("create-template.sql");
        try{
            InputStream inputStream = new FileInputStream(template);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String str = bufferedReader.readLine();
            while (null!=str){
                str = matchingParam(str, parameter);
                bufferedWriter.write(str);
            }
            bufferedWriter.flush();
            bufferedWriter.close();

        }catch (Exception e ){
            e.printStackTrace();
        }

        return  file;
    }

    public String matchingParam(String str, Map<String , String> parameter){

        String regx = "";

        return null;
    }



}
