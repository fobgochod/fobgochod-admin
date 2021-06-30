package com.fobgochod.domain;

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
     * 非预期异常
     */
    Unexpected;
}
