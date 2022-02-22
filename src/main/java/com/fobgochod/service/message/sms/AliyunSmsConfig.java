package com.fobgochod.service.message.sms;

import com.aliyun.teaopenapi.models.Config;
import com.fobgochod.service.message.SecretConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunSmsConfig {

    @Autowired
    private SecretConfig secretConfig;

    @Bean
    public com.aliyun.dysmsapi20170525.Client smsClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(secretConfig.getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(secretConfig.getAccessKeySecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}



