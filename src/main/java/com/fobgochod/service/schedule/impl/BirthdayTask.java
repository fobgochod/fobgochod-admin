package com.fobgochod.service.schedule.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.serializer.Constants;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * 生日提醒
 *
 * @author Xiao
 * @date 2022/2/22 22:28
 */
@Component
public class BirthdayTask extends TaskService {

    private static final Logger logger = LoggerFactory.getLogger(BirthdayTask.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private com.aliyun.dysmsapi20170525.Client smsClient;

    @Override
    public void execute() throws Exception {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS006.name());
        if (task != null) {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                int before = Period.between(user.getBirth(), LocalDate.now()).getDays();
                switch (before) {
                    case 0:
                    case 1:
                    case 3:
                        SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(user.getTelephone())
                                .setSignName("周萧")
                                .setTemplateCode("SMS_234408395")
                                .setTemplateParam(String.format("{\"name\":\"%s\",\"birth\":\"%s\"}", user.getName(), user.getBirth()
                                        .format(Constants.DATE_FORMATTER)));
                        smsClient.sendSms(sendSmsRequest);
                        break;
                }
            }
        }
    }
}
