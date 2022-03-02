package com.fobgochod.service.schedule.impl;

import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.Medicine;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MedicineTask.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;

    @Override
    public void execute() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS005.name());
        if (task != null) {
            List<String> userIds = medicineRepository.findUserIds();
            for (String userId : userIds) {
                List<Medicine> medicines = medicineRepository.findByUserId(userId);
                User user = userRepository.findByCode(userId);
                if (user != null && user.getTelephone() != null) {
                    // 1、吃药提醒
                    MedicType type = MedicType.type();
                    boolean match = medicines.stream().anyMatch(medicine -> {
                        boolean need = (type == MedicType.MORNING && medicine.getMorning() > 0)
                                || (type == MedicType.NOON && medicine.getNoon() > 0)
                                || (type == MedicType.NIGHT && medicine.getNight() > 0);
                        if (need) {
                            MedicineRecord record = medicineRecordRepository.findRecord(medicine.getId(), type.getName());
                            return record == null;
                        }
                        return false;
                    });
                    if (match) {
                        aliyunSmsService.medicine(user.getTelephone(), user.getName(), MedicType.type());

                        boolean forget = type == MedicType.MORNING && now.getHour() == MedicType.MORNING.getEnd()
                                || type == MedicType.NOON && now.getHour() == MedicType.NOON.getEnd()
                                || type == MedicType.NIGHT && now.getHour() == MedicType.NIGHT.getEnd();
                        if (forget) {
                            aliyunSmsService.medicine(user.getContacts().get(0), user.getName(), MedicType.type());
                        }
                    }
                    // 2、挂号提醒
                    int remain = medicines.stream().map(Medicine::getRemain).min(Comparator.naturalOrder()).orElse(-1);
                    if (now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getDayOfWeek() == DayOfWeek.TUESDAY) {
                        if (now.getHour() == 10 || now.getHour() == 22) {
                            aliyunSmsService.registration(user.getTelephone(), user.getName(), remain);
                        }
                    }
                }
            }
        }
    }
}
