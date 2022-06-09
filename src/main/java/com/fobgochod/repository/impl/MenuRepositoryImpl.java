package com.fobgochod.repository.impl;

import com.fobgochod.entity.menu.Menu;
import com.fobgochod.repository.MenuRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class MenuRepositoryImpl extends BaseEntityRepository<Menu> implements MenuRepository {

    @Override
    public List<Menu> findByParentId(String parentId) {
        return mongoTemplate.find(Query.query(Criteria.where("parentId").in(parentId)), getEntityClass());
    }

    @Override
    public List<Menu> findByIdIn(Collection<String> ids) {
        return mongoTemplate.find(Query.query(Criteria.where("id").in(ids)), getEntityClass());
    }
}
