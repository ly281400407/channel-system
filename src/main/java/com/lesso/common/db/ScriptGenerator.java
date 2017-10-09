package com.lesso.common.db;

import com.ibatis.common.resources.Resources;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 脚本生成器
 * 生成用户建库脚本
 */
public class ScriptGenerator {

    private static Logger logger = Logger.getLogger(ScriptGenerator.class);

    /**
     * 生成执行sql脚本
     * @param parameter
     * @return
     */
    public File generatorScript(Map<String, String> parameter, String template) throws IOException {

        long systime = System.currentTimeMillis();
        String fileName = systime+".sql";
        File file = new File(fileName);
        file.createNewFile();

        try{
            InputStream inputStream = Resources.getResourceAsStream(template);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String str = bufferedReader.readLine();
            while (null!=str){
                str = matchingParam(str, parameter);
                bufferedWriter.write(str);
                bufferedWriter.newLine();
                str = bufferedReader.readLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();

        }catch (IOException e ){
            logger.error(e.getMessage());
            throw e;
        }

        return  file;
    }

    /**
     * 匹配字符串中参数，将匹配参数替换
     * @param str
     * @param parameter
     * @return
     */
    private String matchingParam(String str, Map<String , String> parameter){

        StringBuilder strBuilder = new StringBuilder(str);
        Pattern pattern = Pattern.compile("[${]{1,}([a-zA-z0-9]-*){1,}[}]{1,}");
        Matcher matcher = pattern.matcher(strBuilder);

        while (matcher.find()){
            String matcherKey = matcher.group();
            matcherKey = matcherKey.replace("${", "");
            matcherKey = matcherKey.replace("}", "");

            String value = parameter.get(matcherKey);
            strBuilder.replace(matcher.start(), matcher.end(), value);

            matcher = pattern.matcher(strBuilder);
        }

        return strBuilder.toString();
    }



}
