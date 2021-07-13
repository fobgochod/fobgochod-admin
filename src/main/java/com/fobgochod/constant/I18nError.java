package com.fobgochod.constant;

import com.fobgochod.domain.base.ErrorHandler;

/**
 * 订单中心code
 *
 * @author zhouxiao
 * @date 2019/4/9
 */
public enum I18nError implements ErrorHandler {

    ERROR_10001("20000", "filename.notnull"),
    ERROR_LOGIN("20000", "login.auth.fail");


    private final String errorCode;
    private final String code;

    I18nError(String errorCode, String code) {
        this.errorCode = errorCode;
        this.code = code;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getCode() {
        return code;
    }
}
