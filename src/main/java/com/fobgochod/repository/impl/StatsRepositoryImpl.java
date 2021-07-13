package com.fobgochod.repository.impl;

import com.fobgochod.domain.BucketInc;
import com.fobgochod.domain.BucketStats;
import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.BucketRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @Autowired
    private BucketRepository bucketRepository;

    @Override
    public boolean existsByYearMonth(int year, int month) {
        return mongoTemplate.exists(Query.query(Criteria.where("year").is(year).and("month").is(month)), getEntityClass());
    }

    @Override
    public void deleteByYearMonth(int year, int month) {
        mongoTemplate.remove(Query.query(Criteria.where("year").is(year).and("month").is(month)), getEntityClass());
    }

    @Override
    public Stats findByYearMonth(int year, int month) {
        return mongoTemplate.findOne(Query.query(Criteria.where("year").is(year).and("month").is(month)), getEntityClass());
    }

    @Override
    public Stats findNewest() {
        return mongoTemplate.findOne(new Query().with(Sort.by(Sort.Order.desc("year"), Sort.Order.desc("month"))), getEntityClass());
    }

    @Override
    public List<BucketInc> getBucketInc(String prev, String next) {
        String[] prevs = prev.split("-");
        String[] nexts = next.split("-");
        LocalDate p = LocalDate.of(Integer.parseInt(prevs[0]), Integer.parseInt(prevs[1]), 1);
        LocalDate n = LocalDate.of(Integer.parseInt(nexts[0]), Integer.parseInt(nexts[1]), 1);
        return getBucketInc(p, n);
    }

    @Override
    public List<BucketInc> getBucketInc(LocalDate prev, LocalDate next) {
        Stats prevStats = this.findByYearMonth(prev.getYear(), prev.getMonthValue());
        Stats nextStats = this.findByYearMonth(next.getYear(), next.getMonthValue());

        if (prevStats == null && nextStats == null) {
            return Collections.emptyList();
        }
        Set<String> buckets = new HashSet<>();
        Map<String, Long> prevMap = null;
        Map<String, Long> nextMap = null;
        if (prevStats != null) {
            prevMap = prevStats.getBuckets().stream().collect(Collectors.toMap(BucketStats::getName, BucketStats::getSize));
            Set<String> prevs = prevStats.getBuckets().stream().map(BucketStats::getName).collect(Collectors.toSet());
            buckets.addAll(prevs);
        }
        if (nextStats != null) {
            nextMap = nextStats.getBuckets().stream().collect(Collectors.toMap(BucketStats::getName, BucketStats::getSize));
            Set<String> nexts = nextStats.getBuckets().stream().map(BucketStats::getName).collect(Collectors.toSet());
            buckets.addAll(nexts);
        }

        List<BucketInc> bucketIncs = new ArrayList<>();
        for (String name : buckets) {
            BucketInc bucketInc = new BucketInc();
            bucketInc.setName(name);
            if (prevMap != null && prevMap.get(name) != null) {
                bucketInc.setPrev(prevMap.get(name));
                bucketInc.setPrevGb(DataUtil.byte2Gb(bucketInc.getPrev()));
                bucketInc.setPrevReadable(DataUtil.byteSwitch(bucketInc.getPrev()));
            }
            if (nextMap != null && nextMap.get(name) != null) {
                bucketInc.setNext(nextMap.get(name));
                bucketInc.setNextGb(DataUtil.byte2Gb(bucketInc.getNext()));
                bucketInc.setNextReadable(DataUtil.byteSwitch(bucketInc.getNext()));
            }
            if (bucketInc.getPrev() == null && bucketInc.getNext() != null) {
                bucketInc.setDiffSign("+");
                bucketInc.setDiff(bucketInc.getNext());
                bucketInc.setDiffReadable(DataUtil.byteSwitch(bucketInc.getDiff()));
            } else if (bucketInc.getPrev() != null && bucketInc.getNext() == null) {
                bucketInc.setDiffSign("-");
                bucketInc.setDiff(Math.abs(bucketInc.getPrev()));
                bucketInc.setDiffReadable(DataUtil.byteSwitch(bucketInc.getDiff()));
            } else {
                if (!bucketInc.getNext().equals(bucketInc.getPrev())) {
                    bucketInc.setDiffSign(bucketInc.getNext() > bucketInc.getPrev() ? "+" : "-");
                    bucketInc.setDiff(Math.abs(bucketInc.getNext() - bucketInc.getPrev()));
                    bucketInc.setDiffReadable(DataUtil.byteSwitch(bucketInc.getDiff()));
                }
            }
            bucketIncs.add(bucketInc);
        }
        return bucketIncs.stream().sorted(Comparator.comparing(BucketInc::getNext).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<BucketStats> getBucketStats() {
        List<BucketStats> bucketStats = new ArrayList<>();
        for (Document database : mongoClient.listDatabases()) {
            bucketStats.add(build(database));
        }
        Map<String, BucketStats> dbBuckets = bucketStats.stream().collect(Collectors.toMap(BucketStats::getName, a -> a, (k1, k2) -> k1));
        List<Bucket> buckets = bucketRepository.findAll();
        Map<String, List<String>> bucketsChild = new HashMap<>();
        List<String> dbBucketsName = bucketStats.stream().map(BucketStats::getName).collect(Collectors.toList());
        buckets.forEach(parent -> {
            List<String> childBuckets = dbBucketsName.stream().filter(child -> child.endsWith("_" + parent.getCode())).collect(Collectors.toList());
            bucketsChild.put(parent.getCode(), childBuckets);
        });
        bucketStats.forEach(stats -> {
            List<String> childNames = bucketsChild.get(stats.getName());
            if (childNames != null && !childNames.isEmpty()) {
                List<BucketStats> childStats = new ArrayList<>();
                childNames.forEach(childName -> {
                    childStats.add(dbBuckets.get(childName));
                });
                stats.setChildren(childStats);
            }
        });
        return bucketStats.stream().filter(o -> bucketsChild.containsKey(o.getName()))
                .sorted(Comparator.comparing(BucketStats::getSize).reversed()).collect(Collectors.toList());
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
