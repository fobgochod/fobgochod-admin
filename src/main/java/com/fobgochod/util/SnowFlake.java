package com.fobgochod.util;

/**
 * 雪花id算法
 *
 * @author chenxsa
 */
public class SnowFlake {

    private static volatile SnowFlake instance;

    private SnowFlake() {
        this.dataCenterId = 1;
        this.machineId = 1;
    }

    public static SnowFlake getInstance() {
        if (instance == null) {
            synchronized (SnowFlake.class) {
                if (instance == null) {
                    instance = new SnowFlake();
                }
            }
        }
        return instance;
    }

    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1544965696000L;

    /**
     * 每一部分占用的位数
     * SEQUENCE_BIT 序列号占用的位数
     * MACHINE_BIT 机器标识占用的位数
     * DATA_CENTER_BIT 数据中心占用的位数
     */
    private final static long SEQUENCE_BIT = 6;
    private final static long MACHINE_BIT = 3;
    private final static long DATA_CENTER_BIT = 3;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastStamp = -1L;


    /**
     * Setup DatacenterId & MachineId. Default is 1,1
     *
     * @param dataCenterId 服务器id
     * @param machineId
     */
    public void init(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }

        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public synchronized String get() {
        return String.valueOf(newId());
    }

    /**
     * @return New SnowFlake Id
     */
    public synchronized long newId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return (currStamp - START_STAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATA_CENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

}
