package com.lesso.common.util.sms;

import com.alibaba.fastjson.JSON;
import com.lesso.common.util.PropertyUtil;
import com.lesso.common.util.sms.request.SmsSendRequest;
import com.lesso.common.util.sms.response.SmsSendResponse;
import com.oracle.webservices.internal.api.message.PropertySet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author tianyh
 * @Description:HTTP 请求
 */
public class SmsUtil {

    private static final String charset = "utf-8";
    // 用户平台API账号(非登录账号,示例:N1234567)
    private static String account = "N0513457";
    // 用户平台API密码(非登录密码)
    private static String pswd = "9JbuXD7Asie0a0";

    private static String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";

    private static void init(){
        account= PropertyUtil.getProperty("config.properties","sms.account");
        pswd= PropertyUtil.getProperty("config.properties","sms.pswd");
        smsSingleRequestServerUrl= PropertyUtil.getProperty("config.properties","sms.serverUrl");
    }



    public static boolean sendMsg(String msg, String phone) throws UnsupportedEncodingException {
        init();
        //状态报告
        String report= "true";

        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        //System.out.println("before request string is: " + requestJson);

        String response = SmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

        //System.out.println("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        if(smsSingleResponse!=null && "0".equals(smsSingleResponse.getCode())){
              return true;
        }
        return false;
    }

    /**
     *
     * @author tianyh
     * @Description
     * @param path
     * @param postContent
     * @return String
     * @throws
     */
    public static String sendSmsByPost(String path, String postContent) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.connect();
            OutputStream os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                // 开始获取数据
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
