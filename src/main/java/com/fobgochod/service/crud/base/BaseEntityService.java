package com.fobgochod.service.crud.base;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.entity.BaseEntity;
import com.fobgochod.service.mongo.EntityCollection;
import com.fobgochod.util.QueryUtil;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public abstract class BaseEntityService<T extends BaseEntity> extends EntityCollection<T>
        implements EntityService<T> {

    @Override
    public String insert(T data) {
        if (data.getId() == null) {
            data.setId(SnowFlake.getInstance().get());
        }
        UserUtil.setCreateFields(data);
        MongoCollection<T> mongoCollection = this.getCollection();
        mongoCollection.insertOne(data);
        return data.getId();
    }

    @Override
    public void deleteById(String id) {
        MongoCollection<T> mongoCollection = this.getCollection();
        mongoCollection.deleteOne(Filters.eq(BaseField.ID, id));
    }

    @Override
    public long deleteByIdIn(Collection<String> ids) {
        MongoCollection<T> mongoCollection = this.getCollection();
        DeleteResult deleteResult = mongoCollection.deleteMany(Filters.in(BaseField.ID, ids));
        return deleteResult.getDeletedCount();
    }

    @Override
    public void update(T data) {
        MongoCollection<T> mongoCollection = this.getCollection();
        Bson filter = Filters.eq(BaseField.ID, data.getId());
        T old = mongoCollection.find(filter).first();
        if (old != null) {
            UserUtil.setModifyFields(data, old);
            mongoCollection.findOneAndReplace(filter, data);
        }
    }

    @Override
    public T findById(String id) {
        MongoCollection<T> mongoCollection = this.getCollection();
        return mongoCollection.find(Filters.eq(BaseField.ID, id)).first();
    }

    @Override
    public boolean existsById(String id) {
        MongoCollection<T> mongoCollection = this.getCollection();
        long documents = mongoCollection.countDocuments(Filters.eq(BaseField.ID, id));
        return documents > 0;
    }

    @Override
    public List<T> findAll() {
        MongoCollection<T> mongoCollection = this.getCollection();
        MongoCursor<T> iterator = mongoCollection.find().iterator();
        List<T> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public List<T> findAll(T filter) {
        MongoCollection<T> mongoCollection = this.getCollection();
        MongoCursor<T> iterator = mongoCollection.find(QueryUtil.filter(filter)).iterator();
        List<T> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public PageData<T> findByPage(Page page) {
        if (page == null) {
            page = new Page();
        }
        MongoCollection<T> mongoCollection = this.getCollection();
        long total = mongoCollection.countDocuments(page.filter());
        if (total <= 0) {
            return PageData.zero();
        }
        MongoCursor<T> iterator = mongoCollection.find(page.filter()).sort(page.sort())
                .skip(page.skip()).limit(page.limit()).iterator();
        List<T> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return PageData.data(total, lists);
    }

    @Override
    public PageData<T> findCondByPage(Page<T> page) {
        return PageData.zero();
    }

    @Override
    public void dropCollection() {
        MongoCollection<T> mongoCollection = this.getCollection();
        mongoCollection.drop();
    }

    @Override
    public T save(T data) {
        if (data.getId() == null) {
            data.setId(SnowFlake.getInstance().get());
        }
        MongoCollection<T> mongoCollection = this.getCollection();
        mongoCollection.insertOne(data);
        return data;
    }

    @Override
    public void update(Bson filter, Bson update) {
        MongoCollection<T> mongoCollection = this.getCollection();
        mongoCollection.updateOne(filter, update);
    }
}
