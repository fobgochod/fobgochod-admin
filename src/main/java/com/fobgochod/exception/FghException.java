package com.fobgochod.exception;

import com.fobgochod.domain.base.ErrorHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 异常基类
 *
 * @author seven
 * @date 2020/5/17
 */
public abstract class FghException extends RuntimeException {

    protected String code;
    protected String message;
    protected ErrorHandler handler;

    public FghException() {
        super();
    }

    public FghException(ErrorHandler handler) {
        super(handler.getErrorMessage());
        this.setHandler(handler);
    }

    public FghException(ErrorHandler handler, Object... args) {
        super(handler.getErrorMessage(args));
        this.setErrorHandler(handler, args);
    }

    public ErrorType getErrorType() {
        return ErrorType.Unexpected;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ErrorHandler getHandler() {
        return handler;
    }

    private void setHandler(ErrorHandler handler) {
        this.code = handler.getErrorCode();
        this.message = handler.getErrorMessage();
        this.handler = handler;
    }

    private void setErrorHandler(ErrorHandler errorHandler, Object... args) {
        this.code = errorHandler.getErrorCode();
        this.message = errorHandler.getErrorMessage(args);
        this.handler = errorHandler;
    }
}
