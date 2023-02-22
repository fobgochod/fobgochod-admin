package com.fobgochod.support;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.enumeration.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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

    private Exception ex;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        this.ex = ex;
        return super.resolveException(request, response, handler, ex);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, ErrorAttributeOptions options) {
        Map<String, Object> defaultMap = super.getErrorAttributes(requestAttributes, options);
        logger.error("", ex);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("type", ErrorType.Unexpected);
        map.put("code", I18nCode.UNEXPECTED.getCode());
        map.put("message", defaultMap.get("message"));
        map.put("path", defaultMap.get("path"));
        map.put("success", Boolean.FALSE);
        return map;
    }
}
