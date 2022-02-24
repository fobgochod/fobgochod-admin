package com.fobgochod.exception;

import com.fobgochod.domain.base.I18nHandler;
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

    public UnauthorizedException(I18nHandler handler) {
        super(handler);
    }

    public UnauthorizedException(I18nHandler handler, Object... args) {
        super(handler, args);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.Unauthorized;
    }
}
