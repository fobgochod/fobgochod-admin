package com.fobgochod.domain.base;

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
    UNEXPECTED("0x0", "system.error.unexpected"),
    /**
     * 参数校验失败
     */
    VALIDATION("20001", "system.error.validation"),
    /**
     * 中间件服务出错
     */
    BUSINESS("20002", "system.error.business"),
    /**
     * 数据服务出错
     */
    DATA_ACCESS("20003", "system.error.dataAccess"),
    /**
     * IAM 用户token无效
     */
    USER_TOKEN_INVALID("20004", "system.error.userTokenInvalid"),
    /**
     * IAM app token无效
     */
    APP_TOKEN_INVALID("20005", "system.error.appTokenInvalid"),
    /**
     * 此API已经过时
     */
    OBSOLETE("20006", "system.error.obsolete"),
    /**
     * 404 页面不存在
     */
    NOT_FOUND("20404", "system.error.notfound");


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
