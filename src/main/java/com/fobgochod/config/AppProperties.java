package com.fobgochod.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public class AppProperties {

    private String baseUri;
    private String version;
    private String buildTime;
    private String wechatUri;

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getWechatUri() {
        return wechatUri;
    }

    public void setWechatUri(String wechatUri) {
        this.wechatUri = wechatUri;
    }
}
