package com.fobgochod.repository;

import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.base.EntityRepository;
import org.springframework.data.repository.query.Param;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface TaskRepository extends EntityRepository<Task> {

    boolean existsByCode(String code);

    Task findValidTaskByCode(@Param("code") String code);

    Task findByClassName(@Param("className") String className);

    boolean lock(String id);

    void unlock(String id);
}
