package com.fobgochod.auth.handler;

import com.fobgochod.util.I18nUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 处理自定义多语言
 *
 * @author seven
 * @date 2020/6/13
 */
public class FobLocaleResolver implements LocaleResolver {

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
