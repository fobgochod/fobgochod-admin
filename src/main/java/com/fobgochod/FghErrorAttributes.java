package com.fobgochod;

import com.fobgochod.domain.base.CommonErrorCode;
import com.fobgochod.domain.enumeration.ErrorType;
import com.fobgochod.exception.FghLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 错误处理
 *
 * @author zhouxiao
 * @date 2020/8/20
 */
@Component
public class FghErrorAttributes extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(FghErrorAttributes.class);

    @Value("${spring.application.name}")
    private String sourceId;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, ErrorAttributeOptions options) {
        Map<String, Object> defaultMap = super.getErrorAttributes(requestAttributes, options);
        String message = String.valueOf(defaultMap.get("message"));
        String path = String.valueOf(defaultMap.get("path"));
        logger.error(FghLog.getLog(message, path));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("code", defaultMap.get("status"));
        map.put("message", message);
        map.put("path", path);
        map.put("success", false);

        map.put("sourceId", sourceId.toUpperCase());
        map.put("errorType", ErrorType.System);
        map.put("errorCode", CommonErrorCode.UNEXPECTED.getErrorCode());
        map.put("errorMessage", CommonErrorCode.UNEXPECTED.getErrorMessage());
        map.put("errorInstructors", Collections.emptyMap());
        return map;
    }
}
