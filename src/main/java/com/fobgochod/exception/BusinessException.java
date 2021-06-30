package com.fobgochod.exception;

import com.fobgochod.domain.CommonErrorCode;
import com.fobgochod.domain.ErrorHandler;
import com.fobgochod.domain.ErrorType;

/**
 * 业务异常
 *
 * @author seven
 * @date 2020/5/16
 */
public class BusinessException extends DapException {

    public BusinessException() {
        super();
    }

    public BusinessException(ErrorHandler errorHandler) {
        super(errorHandler);
    }

    public BusinessException(ErrorHandler errorHandler, Object[] args) {
        super(errorHandler, args);
    }

    public BusinessException(ErrorHandler errorHandler, String message) {
        super(errorHandler, message);
    }

    public BusinessException(ErrorHandler errorHandler, Object[] args, String message) {
        super(errorHandler, args, message);
    }

    public BusinessException(String message) {
        super(CommonErrorCode.BUSINESS, message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.setDefault();
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.setDefault();
    }

    void setDefault() {
        this.errorCode = CommonErrorCode.BUSINESS.getErrorCode();
        this.errorMessage = CommonErrorCode.BUSINESS.getErrorMessage();
        this.errorHandler = CommonErrorCode.BUSINESS;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.Business;
    }
}
