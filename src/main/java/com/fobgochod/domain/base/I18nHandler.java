package com.fobgochod.domain.base;

import com.fobgochod.util.I18nUtils;

/**
 * 错误码国际化
 *
 * @author seven
 * @date 2020/6/7
 */
public interface I18nHandler {

    /**
     * the custom error code, such as 20000
     *
     * @return error code
     */
    Integer getCode();

    /**
     * the code to lookup up, such as 'dap.middleware.error.system'
     *
     * @return i18n path
     */
    String getPath();

    /**
     * code对应的多语言信息
     *
     * @return
     */
    default String getMessage() {
        return I18nUtils.getMessage(this.getPath());
    }

    /**
     * code对应的多语言信息
     *
     * @param args
     * @return
     */
    default String getMessage(Object... args) {
        return I18nUtils.getMessage(this.getPath(), args);
    }
}
