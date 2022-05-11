package com.fobgochod.repository;

import com.fobgochod.entity.spda.SmsRecord;
import com.fobgochod.repository.base.EntityRepository;

/**
 * SmsRecordRepository.java
 *
 * @author Xiao
 * @date 2022/3/1 0:20
 */
public interface SmsRecordRepository extends EntityRepository<SmsRecord> {

    SmsRecord findByTelephoneAndCode(String telephone, String captchaCode);
}
