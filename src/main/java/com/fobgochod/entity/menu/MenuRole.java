package com.fobgochod.entity.menu;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 角色菜单
 *
 * @author Xiao
 * @date 2022/5/23 22:51
 */
@Document("menu_role")
public class MenuRole extends BaseEntity {

    private String roleId;
    private String menuId;
    private Boolean checked;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
