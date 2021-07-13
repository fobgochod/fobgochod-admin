package com.fobgochod.exception;

import com.fobgochod.domain.base.CommonErrorCode;
import com.fobgochod.domain.base.ErrorHandler;
import com.fobgochod.domain.enumeration.ErrorType;

/**
 * 业务异常
 *
 * @author seven
 * @date 2020/5/16
 */
public class BusinessException extends FghException {

    public BusinessException() {
        super();
    }

    public BusinessException(ErrorHandler handler) {
        super(handler);
    }

    public BusinessException(ErrorHandler handler, Object... args) {
        super(handler, args);
    }

    public BusinessException(String message) {
        super(CommonErrorCode.BUSINESS, message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.Business;
    }
}
