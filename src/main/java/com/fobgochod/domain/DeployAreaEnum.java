package com.fobgochod.domain;

/**
 * 系统环境
 *
 * @author zhouxiao
 * @date 2021/2/2
 */
public enum DeployAreaEnum {

    /**
     * 本地
     */
    Local,
    /**
     * 开发区
     */
    Develop,
    /**
     * PaaS验证区
     */
    AliPaaS,
    /**
     * 阿里测试区
     */
    AliTest,
    /**
     * 微软测试区
     */
    AzureTest,
    /**
     * 阿里正式区
     */
    AliProd,
    /**
     * 微软正式区
     */
    AzureProd,
    /**
     * 地端环境
     */
    Ground
}
