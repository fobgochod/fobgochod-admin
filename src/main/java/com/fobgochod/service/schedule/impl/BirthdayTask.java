package com.fobgochod.service.schedule.impl;

import cn.hutool.core.date.ChineseDate;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 生日提醒
 *
 * @author Xiao
 * @date 2022/2/22 22:28
 * @see TaskIdEnum#TS007
 */
@Component
public class BirthdayTask extends TaskService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliyunSmsService aliyunSmsService;

    @Override
    public void execute() throws Exception {
        List<User> users = userRepository.findAll().stream().filter(user -> user.getBirth() != null).toList();
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
                case 0, 1, 2, 5, 7, 10 -> {
                    String telephones = String.join(",", user.getContacts());
                    if (StringUtils.hasLength(telephones)) {
                        aliyunSmsService.birthday(user.getTenantCode(), telephones, user.getName(), user.getBirth());
                    }
                }
            }
        }
    }
}
