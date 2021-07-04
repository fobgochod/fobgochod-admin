package com.fobgochod.exception;

import com.fobgochod.domain.ErrorHandler;
import com.fobgochod.domain.ErrorType;

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

    public UnauthorizedException(ErrorHandler errorHandler) {
        super(errorHandler);
    }

    public UnauthorizedException(ErrorHandler errorHandler, Object[] args) {
        super(errorHandler, args);
    }

    public UnauthorizedException(ErrorHandler errorHandler, String message) {
        super(errorHandler, message);
    }

    public UnauthorizedException(ErrorHandler errorHandler, Object[] args, String message) {
        super(errorHandler, args, message);
    }

    /**
     * 兼容旧的错误码
     */
    @Deprecated
    public UnauthorizedException(ErrorHandler errorHandler, int code) {
        super(errorHandler);
        this.code = code;
    }

    @Deprecated
    public UnauthorizedException(ErrorHandler errorHandler, int code, String message) {
        super(errorHandler, message);
        this.code = code;
    }

    /**
     * 兼容旧的错误码
     */
    @Deprecated
    public UnauthorizedException(ErrorHandler errorHandler, Object[] args, int code) {
        super(errorHandler, args);
        this.code = code;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.Business;
    }
}
