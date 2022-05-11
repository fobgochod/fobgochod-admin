package com.fobgochod.repository;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.entity.spda.MedicineRecord;
import com.fobgochod.repository.base.EntityRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * MedicineRecord
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface MedicineRecordRepository extends EntityRepository<MedicineRecord> {

    MedicineRecord findRecord(String medicineId, String type);

    MedicineRecord findRecord(String medicineId, String type, LocalDate date);

    List<MedicineRecord> findByMedicineIdIn(Collection<String> ids);

    List<GroupBy> findMedicineCounts(List<String> medicineIds);
}
