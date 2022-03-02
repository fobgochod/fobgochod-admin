package com.fobgochod.service.message.sms;

import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.SmsRecord;

import java.time.LocalDate;

/**
 * AliyunSmsService.java
 *
 * @author Xiao
 * @date 2022/2/28 23:50
 */
public interface AliyunSmsService {

    void test(String telephone, String name);

    void captcha(String telephone);

    void medicine(String telephone, String userName, MedicType type);

    void registration(String telephone, String userName, int remain);

    void birthday(String telephone, String userName, LocalDate birth);

    void sendSms(SmsRecord smsRecord);
}
