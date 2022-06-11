package com.fobgochod.repository;

import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Medicine
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface MedicineRepository extends EntityRepository<Medicine> {

    boolean existsByCode(@Param("code") String code);

    List<Medicine> findByUserCode(@Param("userCode") String userCode, @Param("status") boolean status);

    List<String> findUserCodes();
}
