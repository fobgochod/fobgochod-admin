package com.fobgochod.service.message.sms;

import com.aliyun.teaopenapi.models.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunSmsConfig {

    public static final String ACCESS_KEY_ID = "LTAI5tQYm1touzLkeB27Q7Ea";
    public static final String ACCESS_KEY_SECRET = "dpmoaHQgQR3jxuUgOdXi9YsORftEPM";

    @Bean
    public com.aliyun.dysmsapi20170525.Client smsClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(ACCESS_KEY_ID)
                // 您的AccessKey Secret
                .setAccessKeySecret(ACCESS_KEY_SECRET);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}



