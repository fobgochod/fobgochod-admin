package com.fobgochod.exception;

import com.fobgochod.domain.ErrorHandler;
import com.fobgochod.domain.ErrorType;

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

    public SystemException(ErrorHandler errorHandler) {
        super(errorHandler);
    }

    public SystemException(ErrorHandler errorHandler, Object[] args) {
        super(errorHandler, args);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.System;
    }
}
