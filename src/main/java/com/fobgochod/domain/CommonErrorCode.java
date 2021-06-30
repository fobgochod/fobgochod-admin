package com.fobgochod.domain;

/**
 * 统一错误码
 *
 * @author seven
 * @date 2020/6/7
 */
public enum CommonErrorCode implements ErrorHandler {

    /**
     * 系统执行出错
     */
    UNEXPECTED("0x0", "dap.middleware.error.unexpected"),
    /**
     * 参数校验失败
     */
    VALIDATION("20001", "dap.middleware.error.validation"),
    /**
     * 中间件服务出错
     */
    BUSINESS("20002", "dap.middleware.error.business"),
    /**
     * 数据服务出错
     */
    DATA_ACCESS("20003", "dap.middleware.error.dataAccess"),
    /**
     * IAM 用户token无效
     */
    USER_TOKEN_INVALID("20004", "dap.middleware.error.userTokenInvalid"),
    /**
     * IAM app token无效
     */
    APP_TOKEN_INVALID("20005", "dap.middleware.error.appTokenInvalid"),
    /**
     * 此API已经过时
     */
    OBSOLETE("20006", "dap.middleware.error.obsolete"),
    /**
     * 404 页面不存在
     */
    NOT_FOUND("20404", "dap.middleware.error.notfound");


    private String errorCode;
    private String code;

    CommonErrorCode(String errorCode, String code) {
        this.errorCode = errorCode;
        this.code = code;
    }

    public static boolean containsCode(String code) {
        for (CommonErrorCode value : CommonErrorCode.values()) {
            if (value.getCode().equals(code)) {
                return true;
            }
        }
        return false;
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
