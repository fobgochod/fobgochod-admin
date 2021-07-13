package com.fobgochod.domain.base;

import com.fobgochod.util.I18nUtils;

/**
 * 错误码国际化
 *
 * @author seven
 * @date 2020/6/7
 */
public interface ErrorHandler {

    /**
     * the custom error code, such as '200001'
     *
     * @return error code
     */
    String getErrorCode();

    /**
     * the code to lookup up, such as 'dap.middleware.error.system'
     *
     * @return i18n path
     */
    String getCode();

    /**
     * code对应的多语言信息
     *
     * @return
     */
    default String getErrorMessage() {
        return I18nUtils.getMessage(this.getCode());
    }

    /**
     * code对应的多语言信息
     *
     * @param args
     * @return
     */
    default String getErrorMessage(Object... args) {
        return I18nUtils.getMessage(this.getCode(), args);
    }
}
