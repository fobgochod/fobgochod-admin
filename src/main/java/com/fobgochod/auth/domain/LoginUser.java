package com.fobgochod.auth.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 登录用户
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
public class LoginUser implements Serializable {

    @NotNull
    private String username;
    @NotNull
    private String pwdhash;

    private String tenantId;

    private LoginType loginType = LoginType.token;

    public LoginUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwdhash() {
        return pwdhash;
    }

    public void setPwdhash(String pwdhash) {
        this.pwdhash = pwdhash;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginUser loginUser = (LoginUser) o;
        return username.equals(loginUser.username) && Objects.equals(tenantId, loginUser.tenantId) && loginType == loginUser.loginType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, tenantId, loginType);
    }
}
