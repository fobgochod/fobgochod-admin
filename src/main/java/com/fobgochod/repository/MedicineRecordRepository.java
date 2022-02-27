package com.fobgochod.repository;

import com.fobgochod.domain.GroupBy;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.repository.base.EntityRepository;

import java.util.Collection;
import java.util.List;

/**
 * MedicineRecord
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface MedicineRecordRepository extends EntityRepository<MedicineRecord> {

    MedicineRecord findByMedicineIdAndType(String medicineId, String type);

    List<MedicineRecord> findByMedicineIdIn(Collection<String> ids);

    List<GroupBy> findMedicineCounts();
}
