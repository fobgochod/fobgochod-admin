package com.fobgochod;

import com.fobgochod.service.message.SecretConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class FghApplication {

    public static final String SECRET_RESOURCE_LOCATION = "META-INF/secret.properties";

    public static ConfigurableApplicationContext ac;
    public static Properties properties;

    static {
        try {
            URL url = ClassLoader.getSystemResources(SECRET_RESOURCE_LOCATION).nextElement();
            UrlResource resource = new UrlResource(url);
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load '" + SECRET_RESOURCE_LOCATION + "': " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        FghApplication.ac = SpringApplication.run(FghApplication.class, args);
    }

    @Bean
    public SecretConfig msnConfig() {
        SecretConfig config = new SecretConfig();
        config.setAccessKeyId(properties.getProperty(SecretConfig.ALIYUN_ACCESS_KEY_ID));
        config.setAccessKeySecret(properties.getProperty(SecretConfig.ALIYUN_ACCESS_KEY_SECRET));
        config.setAppId(properties.getProperty(SecretConfig.WECHAT_APP_ID));
        config.setAppSecret(properties.getProperty(SecretConfig.WECHAT_APP_SECRET));
        return config;
    }
}
