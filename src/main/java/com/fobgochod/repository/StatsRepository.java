package com.fobgochod.repository;

import com.fobgochod.repository.base.EntityRepository;
import com.fobgochod.entity.admin.Stats;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface StatsRepository extends EntityRepository<Stats> {

    void deleteByYearMonth(String year, String month);

    Stats findNewest();

}
