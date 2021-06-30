package com.fobgochod.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 各自数据处理
 *
 * @author zhouxiao
 * @date 2020/11/20
 */
public class DataUtil {

    public static final Long B = 1L;
    public static final Long KB = 1024L;
    public static final Long MB = 1024 * 1024L;
    public static final Long GB = 1024 * 1024 * 1024L;

    public static final BigDecimal BIG_B = BigDecimal.valueOf(B);
    public static final BigDecimal BIG_KB = BigDecimal.valueOf(KB);
    public static final BigDecimal BIG_MB = BigDecimal.valueOf(MB);
    public static final BigDecimal BIG_GB = BigDecimal.valueOf(GB);

    /**
     * Bytes转GB
     *
     * @param bytes
     * @return
     */
    public static Double byte2Gb(long bytes) {
        return BigDecimal.valueOf(bytes).divide(BIG_GB, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String toFixed(long bytes, BigDecimal unit) {
        return BigDecimal.valueOf(bytes).divide(unit, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String byteSwitch(long bytes) {
        String size;
        if (bytes < 0.1 * KB) {
            //小于0.1KB，则转化成B
            size = toFixed(bytes, BIG_B) + "B";
        } else if (bytes < 0.1 * MB) {
            //小于0.1MB，则转化成KB
            size = toFixed(bytes, BIG_KB) + "KB";
        } else if (bytes < 0.1 * GB) {
            //小于0.1GB，则转化成MB
            size = toFixed(bytes, BIG_MB) + "MB";
        } else {
            //其他转化成GB
            size = toFixed(bytes, BIG_GB) + "GB";
        }

        //获取小数点处的索引
        int index = size.indexOf(".");
        //获取小数点后两位的值
        String dou = size.substring(index + 1, index + 3);
        //判断后两位是否为00，如果是则删除00
        if ("00".equals(dou)) {
            return size.substring(0, index) + size.substring(index + 3);
        }
        return size;
    }

    public static List<LocalDate> getDaysPeriod(int period) {
        List<LocalDate> days = new ArrayList<>();
        LocalDate firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        if (period <= 0 || period > 12) {
            days.add(firstDayOfYear);
        } else {
            for (int i = 0; i < 12 / period; i++) {
                days.add(firstDayOfYear.plusMonths(period * i));
            }
        }
        return days;
    }
}
