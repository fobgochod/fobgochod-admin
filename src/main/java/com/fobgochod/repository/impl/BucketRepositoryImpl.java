package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.repository.BucketRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BucketRepositoryImpl extends BaseEntityRepository<Bucket> implements BucketRepository {

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }
}
