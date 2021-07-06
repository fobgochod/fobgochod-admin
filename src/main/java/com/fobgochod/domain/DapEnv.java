package com.fobgochod.domain;

import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

/**
 * 公共属性
 *
 * @author seven
 * @date 2020/4/12
 */
public class DapEnv extends SystemEnv {

    @Value("${app.version:}")
    private String version;
    @Value("${app.build-time}")
    private String buildTime;
    @Value("${app.language:}")
    private String language;
    @Value("${app.country:}")
    private String country;
    @Value("${app.variant:}")
    private String variant;
    @Value("${spring.profiles.active:}")
    private String active;
    @Value("${server.port}")
    private String port;
    @Value("${dap.middleware.deploy-area:}")
    private DeployAreaEnum deployArea;
    @Value("${dap.middleware.accessLog:}")
    private Boolean accessLog;
    @Value("${dap.middleware.iam.data-center-id:1}")
    private long dataCenterId;
    @Value("${dap.middleware.iam.machine-id:1}")
    private long machineId;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public DeployAreaEnum getDeployArea() {
        return deployArea;
    }

    public void setDeployArea(DeployAreaEnum deployArea) {
        this.deployArea = deployArea;
    }

    public Boolean getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(Boolean accessLog) {
        this.accessLog = accessLog;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public Locale getLocale() {
        return new Locale(language, country, variant);
    }
}
