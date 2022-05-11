package com.fobgochod.repository.impl;

import com.fobgochod.entity.spda.SmsRecord;
import com.fobgochod.repository.SmsRecordRepository;
import com.fobgochod.repository.base.BaseEntityRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class SmsRecordRepositoryImpl extends BaseEntityRepository<SmsRecord> implements SmsRecordRepository {

    @Override
    public SmsRecord findByTelephoneAndCode(String telephone, String captchaCode) {
        return mongoTemplate.findOne(Query.query(Criteria.where("telephone")
                .is(telephone)
                .and("captchaCode")
                .is(captchaCode)), getEntityClass());
    }
}
