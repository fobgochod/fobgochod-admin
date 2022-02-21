package com.fobgochod.repository.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class MedicineRecordRepositoryImpl extends BaseEntityRepository<MedicineRecord> implements MedicineRecordRepository {

    @Override
    public MedicineRecord findByMedicineIdAndType(String medicineId, String type) {
        return mongoTemplate.findOne(Query.query(Criteria.where("medicineId")
                .is(medicineId)
                .and("type")
                .is(type)), getEntityClass());
    }

    @Override
    public List<MedicineRecord> findByMedicineIdIn(Collection<String> medicineIds) {
        return mongoTemplate.find(Query.query(Criteria.where(BaseField.ID).in(medicineIds)), getEntityClass());
    }
}
