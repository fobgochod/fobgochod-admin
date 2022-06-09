package com.fobgochod.api;

import com.fobgochod.auth.holder.AuthoredUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.domain.menu.MenuTree;
import com.fobgochod.entity.admin.Role;
import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.menu.Menu;
import com.fobgochod.entity.menu.MenuRole;
import com.fobgochod.repository.MenuRepository;
import com.fobgochod.repository.MenuRoleRepository;
import com.fobgochod.repository.RoleRepository;
import com.fobgochod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;

    @PostMapping("/menu")
    public ResponseEntity<?> tree(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) AuthoredUser authoredUser) {
        User user = userRepository.findByCode(authoredUser.getUserId());
        Role role = roleRepository.findByCode(user.getRole());
        List<Menu> menus;
        if (!RoleEnum.Admin.name().equals(role.getCode())) {
            List<MenuRole> menuRoles = menuRoleRepository.findByRoleId(role.getId());
            menus = menuRepository.findByIdIn(menuRoles.stream().map(MenuRole::getMenuId).collect(Collectors.toList()));
        } else {
            // 管理员获取所菜单
            menus = menuRepository.findAll();
        }
        Map<String, MenuTree> menuMap = menus.stream().collect(Collectors.toMap(Menu::getId, MenuTree::new, (k1, k2) -> k1));
        List<MenuTree> trees = new ArrayList<>();
        for (MenuTree menu : menuMap.values()) {
            if (menu.getLevel() == 1) {
                trees.add(menu);
            } else {
                MenuTree menuItem = menuMap.get(menu.getParentId());
                menuItem.getChildren().add(menu);
            }
        }
        // 第二层排序
        trees.forEach(tree -> tree.setChildren(tree.getChildren().stream().sorted(Comparator.comparing(MenuTree::getOrder)).collect(Collectors.toList())));
        // 第一层排序并返回
        return ResponseEntity.ok(trees.stream().sorted(Comparator.comparing(MenuTree::getOrder)).collect(Collectors.toList()));
    }
}
