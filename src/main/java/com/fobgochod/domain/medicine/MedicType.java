package com.fobgochod.domain.medicine;

import java.time.LocalTime;

/**
 * MedicType.java
 *
 * @author Xiao
 * @date 2022/2/27 19:39
 */
public enum MedicType {

    TODAY(0, 23, "今天"),
    /**
     * 8-12点
     */
    MORNING(8, 12, "早晨"),
    /**
     * 13-17点
     */
    NOON(13, 17, "中午"),
    /**
     * 18-22
     */
    NIGHT(18, 22, "晚上"),
    /**
     * 拿药
     */
    INPUT(0, 23, "买入"),
    /**
     * 停药
     */
    OUTPUT(0, 23, "卖出");

    private final int start;
    private final int end;
    private final String name;

    MedicType(int start, int end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public static MedicType current() {
        int hour = LocalTime.now().getHour();
        if (hour >= MORNING.getStart() && hour <= MORNING.getEnd()) {
            return MORNING;
        } else if (hour >= NOON.getStart() && hour <= NOON.getEnd()) {
            return NOON;
        } else if (hour >= NIGHT.getStart() && hour <= NIGHT.getEnd()) {
            return NIGHT;
        }
        return TODAY;
    }
}
