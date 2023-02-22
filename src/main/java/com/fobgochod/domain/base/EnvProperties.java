package com.fobgochod.domain.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author seven
 * @date 2021/6/29
 */
@Component
public class EnvProperties extends SystemEnv {

    @Value("${info.version:}")
    private String version;
    @Value("${info.build-time}")
    private String buildTime;
    @Value("${spring.profiles.active:}")
    private String active;
    @Value("${server.port}")
    private String port;

    @Value("${spring.data.mongodb.uri:}")
    private String mongodbUri;
    @Value("${spring.data.mongodb.database:}")
    private String database;

    @Value("${info.base-uri:}")
    private String baseUri;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

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

    public String getMongodbUri() {
        return mongodbUri;
    }

    public void setMongodbUri(String mongodbUri) {
        this.mongodbUri = mongodbUri;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
