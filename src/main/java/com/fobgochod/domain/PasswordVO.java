package com.fobgochod.domain;

/**
 * Bucket初始化
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
public class PasswordVO {

    private String code;
    private String pwdHash;
    private String oldPwdHash;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPwdHash() {
        return pwdHash;
    }

    public void setPwdHash(String pwdHash) {
        this.pwdHash = pwdHash;
    }

    public String getOldPwdHash() {
        return oldPwdHash;
    }

    public void setOldPwdHash(String oldPwdHash) {
        this.oldPwdHash = oldPwdHash;
    }
}
