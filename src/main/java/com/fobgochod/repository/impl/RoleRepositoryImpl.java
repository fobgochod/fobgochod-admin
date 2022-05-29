package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.Role;
import com.fobgochod.repository.RoleRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends BaseEntityRepository<Role> implements RoleRepository {

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public Role findByCode(String code) {
        return mongoTemplate.findOne(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }
}
