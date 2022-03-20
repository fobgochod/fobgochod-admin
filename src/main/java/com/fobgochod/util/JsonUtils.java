package com.fobgochod.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fobgochod.serializer.*;
import org.bson.types.ObjectId;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * json相关的帮助类
 *
 * @author seven
 * @date 2020/5/17
 */
public final class JsonUtils {

    static volatile ObjectMapper objectMapper = null;


    public static ObjectMapper createObjectMapper() {
        if (objectMapper == null) {
            synchronized (JsonUtils.class) {
                if (objectMapper == null) {
                    JavaTimeModule javaTimeModule = new JavaTimeModule();
                    javaTimeModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
                    javaTimeModule.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
                    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
                    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
                    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
                    javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
                    javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
                    javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
                    javaTimeModule.addSerializer(Timestamp.class, new TimestampSerializer());
                    javaTimeModule.addDeserializer(Timestamp.class, new TimestampDeserializer());
                    objectMapper = Jackson2ObjectMapperBuilder.json()
                            .serializationInclusion(JsonInclude.Include.NON_NULL)
                            .failOnUnknownProperties(false)
                            .modules(javaTimeModule)
                            .build();
                    // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
                    // Include.Include.ALWAYS 默认
                    // Include.NON_DEFAULT 属性为默认值不序列化
                    // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
                    // Include.NON_NULL 属性为NULL 不序列化
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    // 允许出现特殊字符和转义符
                    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
                    // 允许出现单引号
                    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                }
            }
        }
        return objectMapper;
    }
}
