package com.fobgochod.repository;

import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.repository.base.EntityRepository;

/**
 * Bucket
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface BucketRepository extends EntityRepository<Bucket> {

    boolean existsByCode(String code);
}
