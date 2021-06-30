package com.fobgochod.constant;

import com.fobgochod.domain.ErrorHandler;

/**
 * 订单中心code
 *
 * @author zhouxiao
 * @date 2019/4/9
 */
public enum I18nError implements ErrorHandler {

    ERROR_10001("20000", "UploadController.E00001"),
    ERROR_10002("20000", "UploadController.E00002"),
    ERROR_10003("20000", "UploadController.E00003"),

    ERROR_LOGIN("20000", "dmc.login.auth");


    private String errorCode;
    private String code;

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
