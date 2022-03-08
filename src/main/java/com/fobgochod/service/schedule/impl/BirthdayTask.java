package com.fobgochod.service.schedule.impl;

import cn.hutool.core.date.ChineseDate;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
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

    private static final Logger logger = LoggerFactory.getLogger(BirthdayTask.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;

    public static void main(String[] args) {
        int days = Period.between(LocalDate.of(2022, 2, 28), LocalDate.now()).getDays();
        System.out.println("days = " + days);
    }

    @Override
    public void execute() throws Exception {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS006.name());
        if (task != null) {
            List<User> users = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getBirth() != null)
                    .collect(Collectors.toList());
            for (User user : users) {
                int before;
                LocalDate now = LocalDate.now();
                LocalDate birth = user.getBirth();
                if (user.isLunar()) {
                    ChineseDate lunarDate = new ChineseDate(now.getYear(), birth.getMonthValue(), birth.getDayOfMonth());
                    LocalDate solarDate = LocalDate.of(lunarDate.getGregorianYear(), lunarDate.getGregorianMonthBase1(), lunarDate.getGregorianDay());
                    before = Period.between(now, solarDate).getDays();
                } else {
                    LocalDate solarDate = LocalDate.of(now.getYear(), birth.getMonth(), birth.getDayOfMonth());
                    before = Period.between(now, solarDate).getDays();
                }

                switch (before) {
                    case 0:
                    case 1:
                    case 5:
                    case 7:
                        aliyunSmsService.birthday(user.getTelephone(), user.getName(), user.getBirth());
                        break;
                }
            }
        }
    }
}
