package com.fobgochod.repository.impl;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.entity.spda.MedicineRecord;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public class MedicineRecordRepositoryImpl extends BaseEntityRepository<MedicineRecord> implements MedicineRecordRepository {

    @Override
    public MedicineRecord findRecord(String medicineId, String type) {
        return findRecord(medicineId, type, LocalDate.now());
    }

    @Override
    public MedicineRecord findRecord(String medicineId, String type, LocalDate date) {
        return mongoTemplate.findOne(Query.query(Criteria.where("medicineId").is(medicineId).and("type").is(type).and("date").is(date)), getEntityClass());
    }

    @Override
    public List<MedicineRecord> findByMedicineIdIn(Collection<String> medicineIds) {
        return mongoTemplate.find(Query.query(Criteria.where("medicineId").in(medicineIds).and("date").is(LocalDate.now())), getEntityClass());
    }

    @Override
    public List<GroupBy> findMedicineCounts(List<String> medicineIds) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("medicineId").in(medicineIds));
        GroupOperation groupOperation = Aggregation.group("medicineId").sum("slice").as("sum").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation)
                .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
        AggregationResults<GroupBy> results = mongoTemplate.aggregate(aggregation, "medicine_record", GroupBy.class);
        return results.getMappedResults();
    }
}
