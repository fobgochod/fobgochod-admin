package com.fobgochod.support.i18n;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class FghMessageSource extends ReloadableResourceBundleMessageSource {

    public FghMessageSource() {
        setBasename("classpath:i18n/messages");
        setDefaultEncoding("UTF-8");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new FghMessageSource());
    }
}
