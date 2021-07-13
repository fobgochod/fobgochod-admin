package com.fobgochod.domain.enumeration;

/**
 * 异常类型
 */
public enum ErrorType {

    /**
     * 系统层异常
     */
    System,

    /**
     * 业务层异常
     */
    Business,

    /**
     * 权限校验异常
     */
    Unauthorized,

    /**
     * 非预期异常
     */
    Unexpected;
}
