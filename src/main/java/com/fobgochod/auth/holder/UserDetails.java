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
public class UserDetails implements Serializable {

    private String userId;
    private String userCode;
    private String userName;
    private String telephone;
    private String role;
    private String tenantId;
    private String tenantCode;
    private String tenantName;
    private String token;

    private UserDetails() {
    }

    public static UserDetails of() {
        return new UserDetails();
    }

    public static UserDetails of(String userId) {
        UserDetails userDetails = of();
        userDetails.setUserCode(userId);
        return userDetails;
    }

    public static UserDetails of(User user, Tenant tenant) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(user.getId());
        userDetails.setUserCode(user.getCode());
        userDetails.setUserName(user.getName());
        userDetails.setTelephone(user.getTelephone());
        userDetails.setRole(user.getRole());
        userDetails.of(tenant);
        return userDetails;
    }

    public void of(Tenant tenant) {
        if (tenant == null) {
            this.tenantId = null;
            this.tenantCode = null;
            this.tenantName = null;
        } else {
            this.tenantId = tenant.getId();
            this.tenantCode = tenant.getCode();
            this.tenantName = tenant.getName();
        }
    }

    public String uniqueKey() {
        return String.format("%s:%s", this.tenantCode == null ? FghConstants.DEFAULT_TENANT : this.tenantCode, this.userCode).toLowerCase();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
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
        UserDetails that = (UserDetails) o;
        return userId.equals(that.userId) && Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tenantId);
    }
}
