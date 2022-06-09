package com.fobgochod.repository;

import com.fobgochod.entity.menu.MenuRole;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRoleRepository extends EntityRepository<MenuRole> {

    void deleteByRoleId(@Param("roleId") String roleId);

    List<MenuRole> findByRoleId(@Param("roleId") String roleId);

    List<MenuRole> findByRoleId(@Param("roleId") String roleId, @Param("checked") boolean checked);
}
