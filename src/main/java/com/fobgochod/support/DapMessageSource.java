package com.fobgochod.support;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class DapMessageSource extends ReloadableResourceBundleMessageSource {

    public DapMessageSource() {
        setBasename("classpath:i18n/messages");
        setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new DapMessageSource());
    }
}
