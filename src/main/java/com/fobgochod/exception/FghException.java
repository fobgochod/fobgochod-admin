package com.fobgochod.exception;

import com.fobgochod.domain.base.I18nHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 异常基类
 *
 * @author seven
 * @date 2020/5/17
 */
public abstract class FghException extends RuntimeException {

    protected I18nHandler handler;

    public FghException() {
        super();
    }

    public FghException(I18nHandler handler) {
        super(handler.getMessage());
        this.handler = handler;
    }

    public FghException(I18nHandler handler, String message) {
        super(message);
        this.handler = handler;
    }

    public FghException(I18nHandler handler, Object... args) {
        super(handler.getMessage(args));
        this.handler = handler;
    }

    public ErrorType getErrorType() {
        return ErrorType.Unexpected;
    }

    public I18nHandler getHandler() {
        return handler;
    }
}
