package com.fobgochod.domain;

import com.fobgochod.entity.admin.Menu;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuTree extends Menu {

    private List<MenuTree> children = new ArrayList<>();

    public MenuTree() {
    }

    public MenuTree(Menu menu) {
        BeanUtils.copyProperties(menu, this);
    }

    public List<MenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }
}
