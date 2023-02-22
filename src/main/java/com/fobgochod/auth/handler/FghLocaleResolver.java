package com.fobgochod.auth.handler;

import com.fobgochod.util.I18nUtils;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 处理自定义多语言
 *
 * @author seven
 * @date 2020/6/13
 */
public class FghLocaleResolver implements LocaleResolver {

    /**
     * 处理逻辑
     * 接口核心方法，获取Locale，并返回
     *
     * @param request 请求
     * @return Locale区域信息
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return I18nUtils.getLocale(request);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    }
}
