package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.medicine.Medicine;
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
 * 监护人提醒
 *
 * @author Xiao
 * @date 2022/8/18 22:15
 * @see TaskIdEnum#TS005
 */
@Component
public class GuardianTask extends TaskService {

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
            if (user != null && user.getContacts() != null && !user.getContacts().isEmpty()) {
                boolean match = medicines.stream().anyMatch(medicine -> {
                    int items = medicineItemRepository.findItems(medicine.getId()).size();
                    int records = medicineRecordRepository.findRecord(medicine.getId()).size();
                    return items > records;
                });
                if (match) {
                    aliyunSmsService.medicine(user.getTenantCode(), String.join(",", user.getContacts()), user.getName(), MedicType.current());
                }
            }
        }
    }
}
