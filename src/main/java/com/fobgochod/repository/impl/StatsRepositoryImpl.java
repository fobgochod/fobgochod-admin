package com.fobgochod.repository.impl;

import com.fobgochod.domain.BucketInc;
import com.fobgochod.domain.BucketStats;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import com.fobgochod.util.DataUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
@Repository
public class StatsRepositoryImpl extends BaseEntityRepository<Stats> implements StatsRepository {

    @Autowired
    private MongoClient mongoClient;

    @Override
    public void deleteByYearMonth(int year, int month) {
        mongoTemplate.remove(Query.query(Criteria.where("year").is(year).and("month").is(month)), getEntityClass());
    }

    @Override
    public Stats findNewest() {
        return mongoTemplate.findOne(new Query().with(Sort.by(Sort.Order.desc("year"), Sort.Order.desc("month"))), getEntityClass());
    }

    @Override
    public BucketStats getBucketStats(String bucket) {
        Document database = mongoClient.listDatabases().filter(Filters.eq("name", bucket)).first();
        return build(database);
    }

    private BucketStats build(Document database) {
        if (database == null) {
            return null;
        }
        BucketStats stats = new BucketStats();
        stats.setName(database.get("name", String.class));
        stats.setSize(database.get("sizeOnDisk", Double.class).longValue());
        stats.setSizeGb(DataUtil.byte2Gb(stats.getSize()));
        stats.setSizeReadable(DataUtil.byteSwitch(stats.getSize()));

        long collections = 0;
        long indexes = 0;
        MongoDatabase databaseClient = mongoClient.getDatabase(stats.getName());
        for (String collection : databaseClient.listCollectionNames()) {
            collections++;
            for (Document ignored : databaseClient.getCollection(collection).listIndexes()) {
                indexes++;
            }
        }
        stats.setCollections(collections);
        stats.setIndexes(indexes);
        stats.setFiles(databaseClient.getCollection("fileinfo").estimatedDocumentCount());
        return stats;
    }
}
