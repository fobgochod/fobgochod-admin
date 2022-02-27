package com.fobgochod.service.schedule.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.fobgochod.domain.medicine.MedicType;
import com.fobgochod.entity.admin.Medicine;
import com.fobgochod.entity.admin.MedicineRecord;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.MedicineRecordRepository;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSms;
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
    private MedicineRepository medicineRepository;
    @Autowired
    private MedicineRecordRepository medicineRecordRepository;
    @Autowired
    private com.aliyun.dysmsapi20170525.Client smsClient;

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
                            MedicineRecord record = medicineRecordRepository.findByMedicineIdAndType(medicine.getId(), type.getName());
                            return record == null;
                        }
                        return false;
                    });
                    if (match) {
                        SendSmsRequest sendSms1 = new SendSmsRequest().setPhoneNumbers(user.getTelephone())
                                .setSignName(AliyunSms.SIGN_NAME)
                                .setTemplateCode(AliyunSms.TC_MEDICINE)
                                .setTemplateParam(String.format("{\"name\":\"%s\",\"time\":\"%s\"}", user.getName(), MedicType.type().getName()));
                        smsClient.sendSms(sendSms1);

                        boolean forget = type == MedicType.MORNING && now.getHour() == MedicType.MORNING.getEnd()
                                || type == MedicType.NOON && now.getHour() == MedicType.NOON.getEnd()
                                || type == MedicType.NIGHT && now.getHour() == MedicType.NIGHT.getEnd();
                        if (forget) {
                            SendSmsRequest sendSms3 = new SendSmsRequest().setPhoneNumbers(user.getContacts().get(0))
                                    .setSignName(AliyunSms.SIGN_NAME)
                                    .setTemplateCode(AliyunSms.TC_MEDICINE)
                                    .setTemplateParam(String.format("{\"name\":\"%s\",\"time\":\"%s\"}", user.getName(), MedicType.type().getName()));
                            smsClient.sendSms(sendSms3);
                        }
                    }
                    // 2、挂号提醒
                    int remain = medicines.stream().map(Medicine::getRemain).min(Comparator.naturalOrder()).orElse(-1);
                    if (now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getDayOfWeek() == DayOfWeek.TUESDAY) {
                        if (now.getHour() == 10 || now.getHour() == 22) {
                            SendSmsRequest sendSms2 = new SendSmsRequest().setPhoneNumbers(user.getTelephone())
                                    .setSignName(AliyunSms.SIGN_NAME)
                                    .setTemplateCode(AliyunSms.TC_REGISTRATION)
                                    .setTemplateParam(String.format("{\"name\":\"%s\",\"day\":\"%s\"}", user.getName(), remain));
                            smsClient.sendSms(sendSms2);
                        }
                    }
                }
            }
        }
    }
}
