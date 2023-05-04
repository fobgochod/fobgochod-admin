package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.entity.medicine.MedicineItem;
import com.fobgochod.entity.medicine.MedicineRecord;
import com.fobgochod.repository.MedicineItemRepository;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 吃药提醒
 *
 * @author zhouxiao
 * @date 2021/1/26
 * @see TaskIdEnum#TS004
 */
@Component
public class MedicineTask extends TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineItemRepository medicineItemRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @Override
    public void execute() throws Exception {
        List<String> userCodes = medicineRepository.findUserCodes();
        for (String userCode : userCodes) {
            List<Medicine> medicines = medicineRepository.findByUserCode(userCode, false);
            User user = userRepository.findByCode(userCode);
            if (user != null && user.getTelephone() != null) {
                boolean match = medicines.stream().anyMatch(medicine -> {
                    MedicineItem item = medicineItemRepository.findItem(medicine.getId());
                    if (item == null || item.getSlice() <= 0) {
                        return false;
                    }
                    MedicineRecord record = medicineRecordRepository.findRecord(medicine.getId(), item.getType());
                    return record == null;
                });
                if (match) {
                    aliyunSmsService.medicine(user.getTenantCode(), user.getTelephone(), user.getName(), MedicType.current());
                }
            }
        }
    }
}
