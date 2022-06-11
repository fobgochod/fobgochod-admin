package com.fobgochod.repository.impl;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MedicineRepositoryImpl extends BaseEntityRepository<Medicine> implements MedicineRepository {

    @Override
    public boolean existsByCode(String code) {
        return mongoTemplate.exists(Query.query(Criteria.where("code").is(code)), getEntityClass());
    }

    @Override
    public List<Medicine> findByUserCode(String userCode, boolean status) {
        return mongoTemplate.find(Query.query(Criteria.where("userCode").is(userCode).and("status").is(status)), getEntityClass());
    }

    @Override
    public List<String> findUserCodes() {
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("userCode").count().as("count"))
                .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
        AggregationResults<GroupBy> results = mongoTemplate.aggregate(aggregation, "medicine", GroupBy.class);
        return results.getMappedResults().stream().map(GroupBy::getId).collect(Collectors.toList());
    }
}
