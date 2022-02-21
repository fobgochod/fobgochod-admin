package com.fobgochod.entity.admin;

import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户
 *
 * @author seven
 * @date 2021/6/28
 */
@Document("user")
public class User extends BaseEntity {

    private String code;
    private String name;
    private String telephone;
    private String wechat;
    private String qq;
    private String email;
    private String password;
    private RoleEnum role = RoleEnum.None;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
