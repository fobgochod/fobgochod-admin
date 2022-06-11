package com.fobgochod.service.message.sms;

/**
 * Aliyun短信参数
 *
 * @author Xiao
 * @date 2022/2/27 22:31
 */
public class AliyunSms {

    public static final String SIGN_NAME = "周萧";
    public static final String SIGN_NAME_ROBO = "萝卜是猫";

    public enum TemplateCode {

        TC_TEST("SMS_82100080", "测试短信"),
        TC_MEDICINE("SMS_234155880", "吃药提醒"),
        TC_BIRTHDAY("SMS_234408395", "生日提醒"),
        TC_REGISTRATION("SMS_235480466", "挂号提醒"),
        TC_CAPTCHA("SMS_235491151", "登陆验证码");

        private final String code;
        private final String name;

        TemplateCode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
