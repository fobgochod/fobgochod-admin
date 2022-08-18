package com.fobgochod.service.schedule.impl;

import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * 挂号提醒
 *
 * @author zhouxiao
 * @date 2021/1/26
 * @see TaskIdEnum#TS006
 */
@Component
public class RegistrationTask extends TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;
    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public void execute() throws Exception {
        List<String> userCodes = medicineRepository.findUserCodes();
        for (String userCode : userCodes) {
            List<Medicine> medicines = medicineRepository.findByUserCode(userCode, false);
            User user = userRepository.findByCode(userCode);
            if (user != null && user.getTelephone() != null && !medicines.isEmpty()) {
                int remain = medicines.stream().map(Medicine::getRemain).min(Comparator.naturalOrder()).orElse(-1);
                aliyunSmsService.registration(user.getTelephone(), user.getName(), remain);
            }
        }
    }
}
