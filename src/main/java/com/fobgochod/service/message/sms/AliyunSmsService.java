package com.fobgochod.service.message.sms;

import com.fobgochod.domain.medicine.MedicType;

import java.time.LocalDate;

/**
 * AliyunSmsService.java
 *
 * @author Xiao
 * @date 2022/2/28 23:50
 */
public interface AliyunSmsService {

    void test(String tenantCode, String telephone, String name);

    void captcha(String tenantCode, String telephone);

    void medicine(String tenantCode, String telephone, String userName, MedicType type);

    void registration(String tenantCode, String telephone, String userName, int remain);

    void birthday(String tenantCode, String telephone, String userName, LocalDate birth);
}
