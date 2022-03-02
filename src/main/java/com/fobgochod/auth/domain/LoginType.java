package com.fobgochod.auth.domain;

/**
 * 登录类型
 *
 * @author zhouxiao
 * @date 2021/3/8
 */
public enum LoginType {

    /**
     * 账号登录
     */
    token,
    /**
     * 验证码
     */
    captcha,
    /**
     * 其它登录
     */
    other
}
