package com.fobgochod;

import com.fobgochod.domain.CommonErrorCode;
import com.fobgochod.domain.ErrorType;
import com.fobgochod.exception.DapLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class FobErrorAttributes extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(FobErrorAttributes.class);

    @Value("${spring.application.name}")
    private String sourceId;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
        Map<String, Object> defaultMap = super.getErrorAttributes(requestAttributes, includeStackTrace);
        String message = String.valueOf(defaultMap.get("message"));
        String path = String.valueOf(defaultMap.get("path"));
        logger.error(DapLog.getLog(message, path));

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
