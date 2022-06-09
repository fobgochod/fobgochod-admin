package com.fobgochod.repository.impl;

import com.fobgochod.entity.menu.MenuRole;
import com.fobgochod.repository.MenuRoleRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuRoleRepositoryImpl extends BaseEntityRepository<MenuRole> implements MenuRoleRepository {

    @Override
    public void deleteByRoleId(String roleId) {
        mongoTemplate.remove(Query.query(Criteria.where("roleId").is(roleId)), getEntityClass());
    }

    @Override
    public List<MenuRole> findByRoleId(String roleId) {
        return mongoTemplate.find(Query.query(Criteria.where("roleId").in(roleId)), getEntityClass());
    }

    @Override
    public List<MenuRole> findByRoleId(String roleId, boolean checked) {
        return mongoTemplate.find(Query.query(Criteria.where("roleId").in(roleId).and("checked").is(checked)), getEntityClass());

    }
}
