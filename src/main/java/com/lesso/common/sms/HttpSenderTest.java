package com.lesso.common.sms;



public class HttpSenderTest {
	public static void main(String[] args) {
		String url = "https://zapi.253.com/msg/HttpBatchSendSM";// 应用地址
		String account = "N2655027";// 账号
		String pswd = "YlUqK5W3CLbfeb";// 密码
		String mobile = "15220174232";// 手机号码，多个号码使用","分割
		String msg = "【悦商云】尊敬的用户：您的校验码是123456，该验证码5分钟内有效，感谢您的支持与使用。";// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String extno = null;// 扩展码

		try {
			String returnString = HttpSender.batchSend(url, account, pswd, mobile, msg, needstatus, extno);
			System.out.println(returnString);
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			// TODO 处理异常
			e.printStackTrace();
		}
	}
}
