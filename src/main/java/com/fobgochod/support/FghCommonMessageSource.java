package com.fobgochod.support;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class FghCommonMessageSource extends ResourceBundleMessageSource {

    public FghCommonMessageSource() {
        setBasename("classpath:i18n/common");
        setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new FghCommonMessageSource());
    }
}