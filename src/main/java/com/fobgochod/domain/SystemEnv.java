package com.fobgochod.domain;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.Date;

/**
 * 系统属性
 *
 * @author seven
 * @date 2020/4/12
 */
public class SystemEnv {

    /**
     * 当前进程运行的主机名
     */
    private String host;
    /**
     * 当前进程所在的IP地址
     */
    private String ipAddress;
    /**
     * 操作系统名称
     */
    private String osName;
    /**
     * 进程号
     */
    private long pid;
    /**
     * Java虚拟机的启动时间
     */
    private Date startTime;
    /**
     * Java虚拟机的正常运行时间
     */
    private String runtime;
    /**
     * 线程总量
     */
    private int threadCount;
    /**
     * 初始化堆内存
     */
    private long heapInit;
    /**
     * 已使用堆内存
     */
    private long heapUsed;
    /**
     * 空闲内存
     */
    private long freeMemory;
    /**
     * 可使用堆内存
     */
    private long heapCommitted;
    /**
     * 最大堆内存
     */
    private long heapMax;

    /**
     * 把byte转换成MB
     *
     * @param bytes
     * @return
     */
    private static long byteToMByte(long bytes) {
        return (bytes / 1024 / 1024);
    }

    /**
     * 获取进程号，适用于windows与linux
     *
     * @return
     */
    private static long tryGetPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            return Long.parseLong(pid);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 获取时间(ms)=几天几小时几分钟几秒
     *
     * @param time
     * @return
     */
    private static String toDuration(long time) {
        long second = time / 1000;
        long day = second / 86400;
        second = second % 86400;
        long hour = second / 3600;
        second = second % 3600;
        long minute = second / 60;
        second = second % 60;

        String duration = second + " 秒";
        if (minute > 0) {
            duration = minute + " 分钟 " + duration;
        }
        if (hour > 0) {
            duration = hour + " 小时 " + duration;
        }
        if (day > 0) {
            duration = day + " 天 " + duration;
        }
        return duration;
    }

    public void refresh() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            this.host = localHost.getHostName();
            this.ipAddress = localHost.getHostAddress();
        } catch (Exception e) {
            this.host = "未知";
        }
        this.osName = System.getProperty("os.name");
        this.pid = tryGetPid();

        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        this.startTime = new Date(runtimeMxBean.getStartTime());
        this.runtime = toDuration(runtimeMxBean.getUptime());

        //线程总数
        this.threadCount = ManagementFactory.getThreadMXBean().getThreadCount();

        // 堆内存使用情况
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        this.heapInit = byteToMByte(heapMemoryUsage.getInit());
        this.heapUsed = byteToMByte(heapMemoryUsage.getUsed());
        this.freeMemory = byteToMByte(Runtime.getRuntime().freeMemory());
        this.heapCommitted = byteToMByte(heapMemoryUsage.getCommitted());
        this.heapMax = byteToMByte(heapMemoryUsage.getMax());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getHeapInit() {
        return heapInit;
    }

    public void setHeapInit(long heapInit) {
        this.heapInit = heapInit;
    }

    public long getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(long heapUsed) {
        this.heapUsed = heapUsed;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public long getHeapCommitted() {
        return heapCommitted;
    }

    public void setHeapCommitted(long heapCommitted) {
        this.heapCommitted = heapCommitted;
    }

    public long getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(long heapMax) {
        this.heapMax = heapMax;
    }
}
