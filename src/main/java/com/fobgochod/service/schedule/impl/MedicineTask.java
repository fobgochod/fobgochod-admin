package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.entity.medicine.Medicine;
import com.fobgochod.entity.medicine.MedicineItem;
import com.fobgochod.entity.medicine.MedicineRecord;
import com.fobgochod.repository.*;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Bucket 总量统计
 *
 * @author zhouxiao
 * @date 2021/1/26
 */
@Component
public class MedicineTask extends TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
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
        LocalDateTime now = LocalDateTime.now();
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS004.name());
        if (task == null) {
            return;
        }
        List<String> userCodes = medicineRepository.findUserCodes();
        for (String userCode : userCodes) {
            List<Medicine> medicines = medicineRepository.findByUserCode(userCode, false);
            User user = userRepository.findByCode(userCode);
            if (user != null && user.getTelephone() != null) {
                // 1、吃药提醒
                boolean match = medicines.stream().anyMatch(medicine -> {
                    MedicineItem item = medicineItemRepository.findItem(medicine.getId());
                    if (item == null || item.getSlice() <= 0) {
                        return false;
                    }
                    MedicineRecord record = medicineRecordRepository.findRecord(medicine.getId(), item.getType());
                    return record == null;
                });
                if (match) {
                    aliyunSmsService.medicine(user.getTelephone(), user.getName(), MedicType.current());
                }
                // 2、每日提醒（每天9点后还没吃完，提醒联系人）
                if (now.getHour() == 21) {
                    boolean match2 = medicines.stream().anyMatch(medicine -> {
                        int items = medicineItemRepository.findItems(medicine.getId()).size();
                        int records = medicineRecordRepository.findRecord(medicine.getId()).size();
                        return items > records;
                    });
                    if (match2) {
                        aliyunSmsService.medicine(String.join(",", user.getContacts()), user.getName(), MedicType.current());
                    }
                }
                // 3、挂号提醒
                int remain = medicines.stream().map(Medicine::getRemain).min(Comparator.naturalOrder()).orElse(-1);
                if (now.getDayOfWeek() == DayOfWeek.TUESDAY) {
                    if (now.getHour() == 9 || now.getHour() == 12 || now.getHour() == 18 || now.getHour() == 22) {
                        aliyunSmsService.registration(user.getTelephone(), user.getName(), remain);
                    }
                }
            }
        }
    }
}
