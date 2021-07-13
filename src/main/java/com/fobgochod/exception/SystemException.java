package com.fobgochod.exception;

import com.fobgochod.domain.base.ErrorHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 系统异常
 *
 * @author seven
 * @date 2020/5/17
 */
public class SystemException extends FghException {

    public SystemException() {
        super();
    }

    public SystemException(ErrorHandler handler) {
        super(handler);
    }

    public SystemException(ErrorHandler handler, Object... args) {
        super(handler, args);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.System;
    }
}
