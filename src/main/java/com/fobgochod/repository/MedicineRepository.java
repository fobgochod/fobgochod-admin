package com.fobgochod.repository;

import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.entity.admin.Medicine;
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

    List<Medicine> findByUserId(@Param("userId") String userId);

    List<String> findUserIds();
}
