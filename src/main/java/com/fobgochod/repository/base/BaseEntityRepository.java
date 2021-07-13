package com.fobgochod.repository.base;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.entity.BaseEntity;
import com.fobgochod.util.QueryUtil;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

@Service
public abstract class BaseEntityRepository<T extends BaseEntity> implements EntityRepository<T> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String insert(T data) {
        if (data.getId() == null) {
            data.setId(SnowFlake.getInstance().get());
        }
        UserUtil.setCreateFields(data);
        return mongoTemplate.insert(data).getId();
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where(BaseField.ID).is(id)), getEntityClass());
    }

    @Override
    public long deleteByIdIn(Collection<String> ids) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where(BaseField.ID).in(ids)), getEntityClass());
        return deleteResult.getDeletedCount();
    }

    @Override
    public void update(T data) {
        T old = mongoTemplate.findById(data.getId(), getEntityClass());
        if (old != null) {
            UserUtil.setModifyFields(data, old);
            mongoTemplate.findAndReplace(Query.query(Criteria.where(BaseField.ID).is(data.getId())), data);
        }
    }

    @Override
    public T findById(String id) {
        return mongoTemplate.findById(id, getEntityClass());
    }

    @Override
    public boolean existsById(String id) {
        return mongoTemplate.exists(Query.query(Criteria.where(BaseField.ID).is(id)), getEntityClass());
    }

    @Override
    public List<T> findAll() {
        return mongoTemplate.findAll(getEntityClass());
    }

    @Override
    public List<T> findAll(T data) {
        return mongoTemplate.find(QueryUtil.query(data), getEntityClass());
    }

    @Override
    public PageData<T> findByPage(Page page) {
        if (page == null) {
            page = new Page();
        }
        long total = mongoTemplate.count(page.query(), getEntityClass());
        if (total <= 0) {
            return PageData.zero();
        }
        List<T> list = mongoTemplate.find(page.query(), getEntityClass());
        return PageData.data(total, list);
    }

    @Override
    public void dropCollection() {
        mongoTemplate.dropCollection(getEntityClass());
    }
}
