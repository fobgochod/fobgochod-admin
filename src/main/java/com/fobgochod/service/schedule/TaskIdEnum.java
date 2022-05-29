package com.fobgochod.service.schedule;

import com.fobgochod.service.schedule.impl.BirthdayTask;
import com.fobgochod.service.schedule.impl.MedicineTask;
import com.fobgochod.service.schedule.impl.StatsTask;
import com.fobgochod.service.schedule.impl.TestTask;

/**
 * 任务ID
 *
 * @author zhouxiao
 * @date 2021/1/26
 */
public enum TaskIdEnum {

    /**
     * 测试任务
     *
     * @see TestTask
     */
    TS001,
    /**
     * 系统统计
     *
     * @see StatsTask
     */
    TS002,
    /**
     * 过期文件移动到回收站
     */
    TS003,
    /**
     * 吃药提醒
     *
     * @see MedicineTask
     */
    TS004,
    /**
     * 生日提醒
     *
     * @see BirthdayTask
     */
    TS005;
}
