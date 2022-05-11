package com.fobgochod.repository;

import com.fobgochod.entity.spda.MedicineItem;
import com.fobgochod.repository.base.EntityRepository;

import java.util.Collection;
import java.util.List;

/**
 * MedicineRecord
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface MedicineItemRepository extends EntityRepository<MedicineItem> {

    MedicineItem findItem(String medicineId);

    List<MedicineItem> findItems(String medicineId);

    List<MedicineItem> findByMedicineIdIn(Collection<String> medicineIds);
}
