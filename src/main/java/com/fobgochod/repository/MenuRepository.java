package com.fobgochod.repository;

import com.fobgochod.entity.menu.Menu;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MenuRepository extends EntityRepository<Menu> {

    List<Menu> findByParentId(@Param("parentId") String parentId);

    List<Menu> findByIdIn(@Param("ids") Collection<String> ids);
}
