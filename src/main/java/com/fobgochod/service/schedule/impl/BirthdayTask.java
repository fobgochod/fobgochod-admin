package com.fobgochod.service.schedule.impl;

import cn.hutool.core.date.ChineseDate;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生日提醒
 *
 * @author Xiao
 * @date 2022/2/22 22:28
 */
@Component
public class BirthdayTask extends TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;

    @Override
    public void execute() throws Exception {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS005.name());
        if (task != null) {
            List<User> users = userRepository.findAll().stream().filter(user -> user.getBirth() != null).collect(Collectors.toList());
            for (User user : users) {
                int before;
                LocalDate now = LocalDate.now();
                LocalDate birth = user.getBirth();
                if (user.getLunar() != null && user.getLunar()) {
                    ChineseDate lunarDate = new ChineseDate(now.getYear(), birth.getMonthValue(), birth.getDayOfMonth());
                    LocalDate solarDate = LocalDate.of(lunarDate.getGregorianYear(), lunarDate.getGregorianMonthBase1(), lunarDate.getGregorianDay());
                    before = (int) (solarDate.toEpochDay() - now.toEpochDay());
                } else {
                    LocalDate solarDate = LocalDate.of(now.getYear(), birth.getMonth(), birth.getDayOfMonth());
                    before = (int) (solarDate.toEpochDay() - now.toEpochDay());
                }

                switch (before) {
                    case 0:
                    case 1:
                    case 5:
                    case 7:
                        String telephones = String.join(",", user.getContacts());
                        if (StringUtils.hasLength(telephones)) {
                            aliyunSmsService.birthday(telephones, user.getName(), user.getBirth());
                        }
                        break;
                }
            }
        }
    }
}
