package com.fobgochod.util;

import com.fobgochod.domain.medicine.MedicType;

import java.time.LocalTime;

/**
 * 日期工具类
 *
 * @author Xiao
 * @date 2022/2/21 23:38
 */
public class DateUtils {

    /**
     * 早晨：0-10
     * 中午：11-16
     * 晚上：17-23
     *
     * @return 吃药时间
     */
    public static MedicType getType() {
        int hour = LocalTime.now().getHour();
        if (hour >= MedicType.MORNING.getStart() && hour <= MedicType.MORNING.getEnd()) {
            return MedicType.MORNING;
        } else if (hour >= MedicType.NOON.getStart() && hour <= MedicType.NOON.getEnd()) {
            return MedicType.NOON;
        } else if (hour >= MedicType.NIGHT.getStart() && hour <= MedicType.NIGHT.getEnd()) {
            return MedicType.NIGHT;
        }
        return MedicType.TODAY;
    }
}
