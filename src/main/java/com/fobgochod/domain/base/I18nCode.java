package com.fobgochod.domain.base;

/**
 * 统一错误码
 *
 * @author seven
 * @date 2020/6/7
 */
public enum I18nCode implements I18nHandler {

    /**
     * 系统执行出错
     */
    UNEXPECTED(20000, "system.error.unexpected"),
    /**
     * 参数校验失败
     */
    VALIDATION(20001, "system.error.validation"),
    /**
     * 系统错误
     */
    SYSTEM(20002, "system.error.system"),
    /**
     * 数据服务出错
     */
    DATA_ACCESS(20003, "system.error.dataAccess"),
    /**
     * 用户token无效
     */
    USER_TOKEN_INVALID(20004, "system.error.userTokenInvalid"),
    /**
     * app token无效
     */
    APP_TOKEN_INVALID(20005, "system.error.appTokenInvalid"),
    /**
     * 此API已经过时
     */
    OBSOLETE(20006, "system.error.obsolete"),
    /**
     * 404 页面不存在
     */
    NOT_FOUND(20404, "system.error.notfound"),


    ERROR_10001(20000, "filename.notnull"),
    ERROR_LOGIN(20000, "login.auth.fail");

    private final Integer code;
    private final String path;

    I18nCode(Integer code, String path) {
        this.code = code;
        this.path = path;
    }

    public static boolean contains(String code) {
        for (I18nCode value : I18nCode.values()) {
            if (value.getPath().equals(code)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getPath() {
        return path;
    }
}
