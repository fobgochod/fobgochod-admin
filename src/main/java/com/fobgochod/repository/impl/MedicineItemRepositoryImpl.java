package com.fobgochod.repository.impl;

import com.fobgochod.entity.spda.MedicineItem;
import com.fobgochod.repository.MedicineItemRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public class MedicineItemRepositoryImpl extends BaseEntityRepository<MedicineItem> implements MedicineItemRepository {

    @Override
    public MedicineItem findItem(String medicineId) {
        List<MedicineItem> items = this.findItems(medicineId);
        LocalTime now = LocalTime.now();
        return items.stream().filter(o -> now.isAfter(o.getStart()) && now.isBefore(o.getEnd()))
                .findFirst().orElse(null);
    }

    @Override
    public List<MedicineItem> findItems(String medicineId) {
        return mongoTemplate.find(Query.query(Criteria.where("medicineId").is(medicineId)), getEntityClass());
    }

    @Override
    public List<MedicineItem> findByMedicineIdIn(Collection<String> medicineIds) {
        return mongoTemplate.find(Query.query(Criteria.where("medicineId").in(medicineIds)), getEntityClass());
    }
}
