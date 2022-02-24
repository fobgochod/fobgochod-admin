package com.fobgochod.exception;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.base.I18nHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 业务异常
 *
 * @author seven
 * @date 2020/5/16
 */
public class SystemException extends FghException {

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(I18nCode.SYSTEM, message);
    }

    public SystemException(I18nHandler handler) {
        super(handler);
    }

    public SystemException(I18nHandler handler, Object... args) {
        super(handler, args);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.System;
    }
}
