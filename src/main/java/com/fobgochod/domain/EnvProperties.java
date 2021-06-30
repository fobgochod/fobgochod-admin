package com.fobgochod.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author seven
 * @date 2021/6/29
 */
@Component
public class EnvProperties extends DapEnv {

    @Value("${spring.data.mongodb.uri:}")
    private String mongodbUri;
    @Value("${spring.data.mongodb.database:}")
    private String database;

    @Value("${app.base-uri:}")
    private String baseUri;

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
}
