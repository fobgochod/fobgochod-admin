package com.fobgochod.repository;

import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 租户
 *
 * @author seven
 * @date 2021/6/28
 */
public interface TenantRepository extends EntityRepository<Tenant> {

    Tenant findByCode(@Param("code") String code);

    boolean existsByCode(@Param("code") String code);

    List<Tenant> findByOwner(@Param("owner") String owner);
}
