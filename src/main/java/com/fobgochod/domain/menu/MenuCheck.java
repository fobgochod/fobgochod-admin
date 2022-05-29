package com.fobgochod.domain.menu;

import java.util.List;

/**
 * MenuCheck.java
 *
 * @author Xiao
 * @date 2022/5/29 0:29
 */
public class MenuCheck {

    private String roleId;
    private List<String> checkedKeys;
    private List<String> halfCheckedKeys;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getCheckedKeys() {
        return checkedKeys;
    }

    public void setCheckedKeys(List<String> checkedKeys) {
        this.checkedKeys = checkedKeys;
    }

    public List<String> getHalfCheckedKeys() {
        return halfCheckedKeys;
    }

    public void setHalfCheckedKeys(List<String> halfCheckedKeys) {
        this.halfCheckedKeys = halfCheckedKeys;
    }
}
