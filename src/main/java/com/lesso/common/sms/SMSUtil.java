package com.lesso.common.sms;

/**
 * Created by czx on 2017/9/29.
 */
public class SMSUtil {

    public static boolean sendCodeMsg(String phone,String code) {
        String url = "https://zapi.253.com/msg/HttpBatchSendSM";// 应用地址
        String account = "N2655027";// 账号
        String pswd = "YlUqK5W3CLbfeb";// 密码
        String msg = "【悦商云】尊敬的用户：您的校验码是"+code+"，该验证码5分钟内有效，感谢您的支持与使用。";// 短信内容
        boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
        String extno = null;// 扩展码

        try {
            String returnString = HttpSender.batchSend(url, account, pswd, phone, msg, needstatus, extno);
            System.out.println(returnString);
            String[] result=returnString.split("\n");
            String[] result1=result[0].split(",");
            if("0".equals(result1[1])){
                return  true;
            }else {
                return false;
            }
            // TODO 处理返回值,参见HTTP协议文档
        } catch (Exception e) {
            // TODO 处理异常
            e.printStackTrace();
            return false;
        }
    }

    public static void  main (String[] args){

//      for(int i=0;i<11;i++){
//          System.out.println(sendCodeMsg("17301935921","000"+i));
//      }

    }
}
