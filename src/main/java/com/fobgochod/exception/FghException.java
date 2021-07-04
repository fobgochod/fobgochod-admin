package com.fobgochod.exception;

import com.fobgochod.domain.ErrorHandler;
import com.fobgochod.domain.ErrorType;
import org.springframework.http.HttpStatus;


/**
 * DAP异常基类
 *
 * @author seven
 * @date 2020/5/17
 */
public abstract class FghException extends RuntimeException {

    protected String errorCode;
    protected String errorMessage;
    protected ErrorHandler errorHandler;
    protected int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public FghException() {
        super();
    }

    public FghException(ErrorHandler errorHandler) {
        super(errorHandler.getErrorMessage());
        this.setErrorHandler(errorHandler);
    }

    public FghException(ErrorHandler errorHandler, Object[] args) {
        super(errorHandler.getErrorMessage(args));
        this.setErrorHandler(errorHandler, args);
    }

    public FghException(ErrorHandler errorHandler, String message) {
        this(message);
        this.setErrorHandler(errorHandler);
    }

    public FghException(ErrorHandler errorHandler, Object[] args, String message) {
        this(message);
        this.setErrorHandler(errorHandler, args);
    }

    public FghException(String message) {
        super(message);
    }

    public FghException(String message, Throwable cause) {
        super(message, cause);
    }

    public FghException(Throwable cause) {
        super(cause);
    }

    public ErrorType getErrorType() {
        return ErrorType.Unexpected;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Deprecated
    public int getCode() {
        return code;
    }

    private void setErrorHandler(ErrorHandler errorHandler) {
        this.errorCode = errorHandler.getErrorCode();
        this.errorMessage = errorHandler.getErrorMessage();
        this.errorHandler = errorHandler;
    }

    private void setErrorHandler(ErrorHandler errorHandler, Object[] args) {
        this.errorCode = errorHandler.getErrorCode();
        this.errorMessage = errorHandler.getErrorMessage(args);
        this.errorHandler = errorHandler;
    }
}
