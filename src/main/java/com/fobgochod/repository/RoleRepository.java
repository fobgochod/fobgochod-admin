package com.fobgochod.repository;

import com.fobgochod.entity.admin.Role;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends EntityRepository<Role> {

    boolean existsByCode(@Param("code") String code);

    Role findByCode(@Param("code") String code);
}
