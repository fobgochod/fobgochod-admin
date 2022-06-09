package com.fobgochod.auth.holder;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * 登录成功对象
 *
 * @author seven
 * @date 2020/6/15
 */
public class AuthoredUser implements Serializable {

    private String sid;
    private String userId;
    private String userName;
    private String telephone;
    private String role;
    private String tenantSid;
    private String tenantId;
    private String tenantName;
    private String token;

    private AuthoredUser() {
    }

    public static AuthoredUser of() {
        return new AuthoredUser();
    }

    public static AuthoredUser of(String userId) {
        AuthoredUser authoredUser = of();
        authoredUser.setUserId(userId);
        return authoredUser;
    }


    public static AuthoredUser of(User user, Tenant tenant) {
        AuthoredUser authoredUser = new AuthoredUser();
        authoredUser.setSid(user.getId());
        authoredUser.setUserId(user.getCode());
        authoredUser.setUserName(user.getName());
        authoredUser.setTelephone(user.getTelephone());
        authoredUser.setRole(user.getRole());
        authoredUser.of(tenant);
        return authoredUser;
    }

    public void of(Tenant tenant) {
        if (tenant == null) {
            return;
        }
        this.tenantSid = tenant.getId();
        this.tenantId = tenant.getCode();
        this.tenantName = tenant.getName();
    }

    public String uniqueKey() {
        return String.format("%s:%s", this.tenantId == null ? FghConstants.DEFAULT_TENANT : this.tenantId, this.userId).toLowerCase();
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTenantSid() {
        return tenantSid;
    }

    public void setTenantSid(String tenantSid) {
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
        return sid.equals(that.sid) && Objects.equals(tenantSid, that.tenantSid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid, tenantSid);
    }
}
