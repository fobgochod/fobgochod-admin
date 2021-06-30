package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TenantRepositoryImpl extends BaseEntityRepository<Tenant> implements TenantRepository {

    @Override
    public Tenant findByCode(String code) {
        return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public List<Tenant> findByOwner(String owner) {
        return mongoTemplate.find(Query.query(Criteria.where("owner").is(owner)), getEntityClass());
    }
}
