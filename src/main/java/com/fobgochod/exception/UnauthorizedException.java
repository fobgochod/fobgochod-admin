package com.fobgochod.exception;

import com.fobgochod.domain.base.ErrorHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 401
 *
 * @author seven
 * @date 2020/5/16
 */
public class UnauthorizedException extends FghException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(ErrorHandler handler) {
        super(handler);
    }

    public UnauthorizedException(ErrorHandler handler, Object... args) {
        super(handler, args);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.Unauthorized;
    }
}
