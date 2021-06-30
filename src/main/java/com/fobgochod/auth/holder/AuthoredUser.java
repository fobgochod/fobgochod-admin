package com.fobgochod.auth.holder;

import java.io.Serializable;
import java.util.Objects;

/**
 * 登录成功对象
 *
 * @author seven
 * @date 2020/6/15
 */
public class AuthoredUser implements Serializable {

    private long sid;
    private String userId;
    private String userName;
    private long tenantSid;
    private String tenantId;
    private String tenantName;
    private String token;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTenantSid() {
        return tenantSid;
    }

    public void setTenantSid(long tenantSid) {
        this.tenantSid = tenantSid;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthoredUser that = (AuthoredUser) o;
        return sid == that.sid &&
                tenantSid == that.tenantSid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid, tenantSid);
    }
}
