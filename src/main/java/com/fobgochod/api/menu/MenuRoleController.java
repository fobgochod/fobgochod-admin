package com.fobgochod.api.menu;

import com.fobgochod.domain.menu.MenuCheck;
import com.fobgochod.entity.menu.Menu;
import com.fobgochod.entity.menu.MenuRole;
import com.fobgochod.entity.admin.Role;
import com.fobgochod.repository.MenuRepository;
import com.fobgochod.repository.MenuRoleRepository;
import com.fobgochod.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menurole")
public class MenuRoleController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MenuCheck body) {
        Role role = roleRepository.findById(body.getRoleId());
        if (role != null) {
            menuRoleRepository.deleteByRoleId(body.getRoleId());
            for (String menuId : body.getCheckedKeys()) {
                Menu menu = menuRepository.findById(menuId);
                MenuRole menuRole = new MenuRole();
                menuRole.setRoleId(body.getRoleId());
                menuRole.setMenuId(menu.getId());
                menuRole.setChecked(true);
                menuRoleRepository.insert(menuRole);
            }
            for (String menuId : body.getHalfCheckedKeys()) {
                Menu menu = menuRepository.findById(menuId);
                MenuRole menuRole = new MenuRole();
                menuRole.setRoleId(body.getRoleId());
                menuRole.setMenuId(menu.getId());
                menuRole.setChecked(false);
                menuRoleRepository.insert(menuRole);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checked")
    public ResponseEntity<?> findByRole(@RequestBody MenuRole body) {
        List<MenuRole> menuRoles = menuRoleRepository.findByRoleId(body.getRoleId(), true);
        return ResponseEntity.ok(menuRoles.stream().map(MenuRole::getMenuId).collect(Collectors.toList()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        menuRoleRepository.dropCollection();
        return ResponseEntity.ok().build();
    }
}
