package com.fobgochod.service.schedule;

/**
 * 定时任务接口
 */
public interface TaskManager {

    /**
     * 根据任务code 启动任务
     *
     * @param taskCode
     */
    boolean start(String taskCode);

    /**
     * 根据任务code 停止任务
     *
     * @param taskCode
     */
    boolean stop(String taskCode);

    /**
     * 根据任务code 重启任务
     *
     * @param taskCode
     */
    boolean restart(String taskCode);

    /**
     * 程序启动时初始化  ==> 启动所有正常状态的任务
     */
    void refresh();

    /**
     * 关闭所有任务
     */
    void shutdown();

    /**
     * 手动调用任务
     *
     * @param taskCode
     */
    void manual(String taskCode);
}
