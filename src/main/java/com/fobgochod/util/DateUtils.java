package com.fobgochod.util;

import java.time.LocalTime;

/**
 * 日期工具类
 *
 * @author Xiao
 * @date 2022/2/21 23:38
 */
public class DateUtils {

    public static final String MORNING = "早晨";
    public static final String NOON = "中午";
    public static final String NIGHT = "晚上";

    /**
     * 早晨：0-10
     * 中午：11-16
     * 晚上：17-23
     *
     * @return 吃药时间
     */
    public static String getType() {
        String time;
        int hour = LocalTime.now().getHour();
        if (hour <= 10) {
            time = MORNING;
        } else if (hour <= 16) {
            time = NOON;
        } else {
            time = NIGHT;
        }
        return time;
    }
}
