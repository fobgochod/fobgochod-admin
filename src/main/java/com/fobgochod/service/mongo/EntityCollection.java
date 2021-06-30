package com.fobgochod.service.mongo;

import com.fobgochod.entity.BaseEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础
 *
 * @author seven
 * @date 2020/11/23
 */
@Service
public abstract class EntityCollection<T extends BaseEntity> {

    private final Map<String, MongoCollection<T>> mongoCollections = new ConcurrentHashMap<>(16);

    @Autowired
    private MongoDatabase mongoDatabase;

    protected String getCollectionName() {
        return getEntityClass().getSimpleName().toLowerCase();
    }

    protected Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public MongoCollection<T> getCollection() {
        return getCollection(getCollectionName());
    }

    private MongoCollection<T> getCollection(String collection) {
        if (!mongoCollections.containsKey(collection)) {
            synchronized (this.mongoCollections) {
                if (!mongoCollections.containsKey(collection)) {
                    MongoCollection<T> mongoCollection = mongoDatabase.getCollection(collection, getEntityClass());
                    mongoCollections.put(collection, mongoCollection);
                }
            }
        }
        return mongoCollections.get(collection);
    }
}
