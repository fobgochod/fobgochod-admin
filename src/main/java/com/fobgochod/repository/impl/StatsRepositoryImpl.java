package com.fobgochod.repository.impl;

import com.fobgochod.entity.admin.Stats;
import com.fobgochod.repository.StatsRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 策略
 *
 * @author zhouxiao
 * @date 2020/11/2
 */
@Repository
public class StatsRepositoryImpl extends BaseEntityRepository<Stats> implements StatsRepository {

    @Override
    public void deleteByYearMonth(String year, String month) {
        mongoTemplate.remove(Query.query(Criteria.where("year").is(year).and("month").is(month)), getEntityClass());
    }

    @Override
    public Stats findNewest() {
        return mongoTemplate.findOne(new Query().with(Sort.by(Sort.Order.desc("year"), Sort.Order.desc("month"))), getEntityClass());
    }
}
