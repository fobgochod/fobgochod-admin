package com.fobgochod.domain;

/**
 * 中间件错误码
 *
 * @author zhouxiao
 * @date 2020/6/10
 */
public enum CommonCode {

    COMMON("20"),
    IAM("21"),
    EMC("22"),
    CMC("23"),
    OMC("24"),
    GMC("25"),
    BOSS("26"),
    PMC("27"),
    DMC("28"),
    CAC("29"),
    EOC("2A");

    CommonCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
