package com.fobgochod.auth.domain;

import com.fobgochod.constant.DmcConstants;
import com.fobgochod.entity.admin.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * JWT用户
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
public class JwtUser implements UserDetails {

    private String id;
    private String username;
    private String password;
    private List<String> buckets;
    private String tenantId;

    public JwtUser() {
        this.buckets = new ArrayList<>();
    }

    public JwtUser(String id, String username) {
        this();
        this.id = id;
        this.username = username;
    }

    public static JwtUser initUser(User user) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(user.getId());
        jwtUser.setUsername(user.getCode());
        return jwtUser;
    }

    public static JwtUser initUser(User user, String tenantId) {
        JwtUser jwtUser = JwtUser.initUser(user);
        jwtUser.setTenantId(tenantId);
        return jwtUser;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<String> buckets) {
        this.buckets = buckets;
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JwtUser jwtUser = (JwtUser) o;
        return Objects.equals(id, jwtUser.id) && username.equals(jwtUser.username) && Objects.equals(tenantId, jwtUser.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, tenantId);
    }

    public String uniqueKey() {
        return String.format("%s::%s", username, tenantId == null ? DmcConstants.DEFAULT_TENANT : tenantId);
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
