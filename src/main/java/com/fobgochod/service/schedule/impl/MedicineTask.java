package com.fobgochod.service.schedule.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.MedicineRepository;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import com.fobgochod.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private com.aliyun.dysmsapi20170525.Client smsClient;

    @Override
    public void execute() throws Exception {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS005.name());
        if (task != null) {
            List<String> userIds = medicineRepository.findUserIds();
            for (String userId : userIds) {
                User user = userRepository.findByCode(userId);
                if (user != null && user.getTelephone() != null) {
                    SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(user.getTelephone())
                            .setSignName("周萧")
                            .setTemplateCode("SMS_234155880")
                            .setTemplateParam(String.format("{\"name\":\"%s\",\"time\":\"%s\"}", task.getName(), DateUtils.getType()));
                    smsClient.sendSms(sendSmsRequest);
                }
            }
        }
    }
}
