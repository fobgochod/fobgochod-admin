package com.fobgochod.util;

import com.fobgochod.constant.GlobalConstants;
import com.fobgochod.domain.CommonErrorCode;
import com.fobgochod.exception.DapException;
import com.fobgochod.support.DapCommonMessageSource;
import com.fobgochod.support.DapMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 功能描述
 *
 * @author seven
 * @date 2020/6/7
 */
public class I18nUtils {

    private static final Logger logger = LoggerFactory.getLogger(I18nUtils.class);
    private static String language;
    private static String country;
    private static final MessageSourceAccessor messageSource = DapMessageSource.getAccessor();
    private static final MessageSourceAccessor messages = DapCommonMessageSource.getAccessor();

    public static Locale getLocale(HttpServletRequest request) {
        if (request.getHeader(GlobalConstants.ACCEPT_LANGUAGE) != null) {
            return request.getLocale();
        } else if (request.getHeader(GlobalConstants.LOCALE) != null) {
            return Locale.forLanguageTag(request.getHeader(GlobalConstants.LOCALE));
        } else if (StringUtils.hasText(language)) {
            return new Locale(language, country);
        } else {
            return Locale.CHINA;
        }
    }

    public static String getMessage(String code) {
        return getMessage(code, null, GlobalConstants.EMPTY);
    }

    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, GlobalConstants.EMPTY, locale);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, GlobalConstants.EMPTY);
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, GlobalConstants.EMPTY, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        return getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
            if (CommonErrorCode.containsCode(code)) {
                return messages.getMessage(code, args, defaultMessage, locale);
            }
            return messageSource.getMessage(code, args, defaultMessage, locale);
        } catch (Exception e) {
            logger.error("{}未找到国际化配置", code);
        }
        return defaultMessage;
    }

    public static String getMessage(DapException custom, Object[] args, String defaultMessage) {
        String code = custom.getErrorHandler().getCode();
        try {
            if (CommonErrorCode.containsCode(code)) {
                return messages.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
            }
            return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            logger.error("{}未找到国际化配置", code);
        }
        return defaultMessage;
    }

    @Value("${app.language:}")
    public void setLanguage(String language) {
        I18nUtils.language = language;
    }

    @Value("${app.country:}")
    public void setCountry(String country) {
        I18nUtils.country = country;
    }
}
