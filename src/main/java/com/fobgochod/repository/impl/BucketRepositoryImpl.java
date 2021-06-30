package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.repository.BucketRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
@Repository
public class BucketRepositoryImpl extends BaseEntityRepository<Bucket> implements BucketRepository {

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public Bucket findByCode(String code) {
        return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public List<Bucket> findByCode(List<String> names) {
        return mongoTemplate.find(Query.query(Criteria.where("code").in(names)), getEntityClass());
    }

    @Override
    public long deleteByCodeAndTenantId(String code, String tenantId) {
        final DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("code").is(code)
                .and("tenantId").is(tenantId)), getEntityClass());
        return result.getDeletedCount();
    }

    @Override
    public List<Bucket> findByOwner(String owner) {
        return mongoTemplate.find(Query.query(Criteria.where("owner").is(owner)), getEntityClass());
    }

    @Override
    public List<Bucket> findByTask(String task) {
        return mongoTemplate.find(Query.query(Criteria.where("task").is(task)), getEntityClass());
    }
}
