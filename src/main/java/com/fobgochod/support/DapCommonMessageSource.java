package com.fobgochod.support;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class DapCommonMessageSource extends ResourceBundleMessageSource {

    public DapCommonMessageSource() {
        setBasename("classpath:i18n/common");
        setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new DapCommonMessageSource());
    }
}
