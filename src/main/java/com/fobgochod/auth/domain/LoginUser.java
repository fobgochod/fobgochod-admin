package com.fobgochod.auth.domain;

import com.fobgochod.constant.FghConstants;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 登录用户
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
public class LoginUser implements UserDetails {

    private String tenantId;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private LoginType loginType = LoginType.token;

    public LoginUser() {
    }

    public LoginUser(String username) {
        this.username = username;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUser loginUser = (LoginUser) o;
        return username.equals(loginUser.username) && Objects.equals(tenantId, loginUser.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, tenantId);
    }

    public String uniqueKey() {
        return String.format("%s::%s", username, tenantId == null ? FghConstants.DEFAULT_TENANT : tenantId);
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
