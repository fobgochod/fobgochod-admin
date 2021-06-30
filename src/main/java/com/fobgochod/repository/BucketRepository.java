package com.fobgochod.repository;

import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface BucketRepository extends EntityRepository<Bucket> {

    boolean existsByCode(String code);

    Bucket findByCode(@Param("code") String code);

    List<Bucket> findByCode(List<String> names);

    long deleteByCodeAndTenantId(@Param("code") String code, @Param("tenantId") String tenantId);

    List<Bucket> findByOwner(@Param("owner") String owner);

    List<Bucket> findByTask(@Param("task") String task);

}
