package com.fobgochod.util;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.exception.FghException;
import com.fobgochod.support.i18n.FghMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

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
    private static final MessageSourceAccessor messages = FghMessageSource.getAccessor();

    public static Locale getLocale(HttpServletRequest request) {
        if (request.getHeader(FghConstants.ACCEPT_LANGUAGE) != null) {
            return request.getLocale();
        } else if (request.getHeader(FghConstants.LOCALE) != null) {
            return Locale.forLanguageTag(request.getHeader(FghConstants.LOCALE));
        } else {
            return Locale.CHINA;
        }
    }

    public static String getMessage(String code) {
        return getMessage(code, null, FghConstants.EMPTY);
    }

    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, FghConstants.EMPTY, locale);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, FghConstants.EMPTY);
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, FghConstants.EMPTY, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        return getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
            return messages.getMessage(code, args, defaultMessage, locale);
        } catch (Exception e) {
            logger.error("{}未找到国际化配置", code);
        }
        return defaultMessage;
    }

    public static String getMessage(FghException custom, Object[] args, String defaultMessage) {
        String code = custom.getHandler().getPath();
        try {
            return messages.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            logger.error("{}未找到国际化配置", code);
        }
        return defaultMessage;
    }
}
