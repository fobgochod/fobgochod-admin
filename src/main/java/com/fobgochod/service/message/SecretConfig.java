package com.fobgochod.service.message;

/**
 * 密钥信息
 *
 * @author fobgo
 * @date 2022/2/22 14:33
 */
public class SecretConfig {

    public static final String ALIYUN_ACCESS_KEY_ID = "aliyun.access_key_id";
    public static final String ALIYUN_ACCESS_KEY_SECRET = "aliyun.access_key_secret";
    public static final String WECHAT_APP_ID = "wechat.app_id";
    public static final String WECHAT_APP_SECRET = "wechat.app_secret";

    private String accessKeyId;
    private String accessKeySecret;
    private String appId;
    private String appSecret;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
