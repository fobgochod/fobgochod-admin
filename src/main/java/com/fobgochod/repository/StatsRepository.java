package com.fobgochod.repository;

import com.fobgochod.domain.BucketInc;
import com.fobgochod.domain.BucketStats;
import com.fobgochod.repository.base.EntityRepository;
import com.fobgochod.entity.admin.Stats;

import java.time.LocalDate;
import java.util.List;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
public interface StatsRepository extends EntityRepository<Stats> {

    boolean existsByYearMonth(int year, int month);

    void deleteByYearMonth(int year, int month);

    Stats findByYearMonth(int year, int month);

    Stats findNewest();

    List<BucketInc> getBucketInc(String prev, String next);

    List<BucketInc> getBucketInc(LocalDate prev, LocalDate next);

    List<BucketStats> getBucketStats();

    BucketStats getBucketStats(String bucket);
}
