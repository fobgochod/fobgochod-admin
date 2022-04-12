package com.fobgochod.service.schedule.impl;

import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.service.schedule.TaskManager;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时任务实现
 */
@Service
public class TaskManagerImpl implements TaskManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(TaskManagerImpl.class);

    private final ReentrantLock lock = new ReentrantLock();
    /**
     * 存放所有可执行任务
     */
    private final Map<String, TaskService> scheduledTaskMap = new ConcurrentHashMap<>();
    /**
     * 存放已经启动的任务map
     */
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Override
    public boolean start(String taskCode) {
        lock.lock();
        try {
            //校验是否已经启动
            if (!this.isStart(taskCode)) {
                //根据key数据库获取任务配置信息
                Task task = taskRepository.findValidTaskByCode(taskCode);
                if (task != null && this.addTask(task)) {
                    //启动任务
                    this.doStartTask(task);
                    return true;
                }
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public boolean stop(String taskCode) {
        boolean startFlag = scheduledFutureMap.containsKey(taskCode);
        if (startFlag) {
            //获取任务实例
            ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(taskCode);
            //关闭实例
            scheduledFuture.cancel(true);
        }
        scheduledTaskMap.remove(taskCode);
        return startFlag;
    }

    @Override
    public boolean restart(String taskCode) {
        //先停止
        this.stop(taskCode);
        //再启动
        return this.start(taskCode);
    }

    @Override
    public void refresh() {
        scheduledTaskMap.clear();
        for (Task task : taskRepository.findAll()) {
            if (task.getDisable()) {
                // 是否启用
                continue;
            }
            try {
                Object target = applicationContext.getBean(task.getClassName(), TaskService.class);
                if (target.getClass() == null) {
                    //定时任务类必须实现ScheduleService接口
                    continue;
                }
                this.restart(task.getCode());
            } catch (Exception e) {
                // 实现类是否填写正常
                logger.error("[" + task.getCode() + "]实现类加载错误" + e.getMessage());
            }
        }
    }

    @Override
    public void shutdown() {
        scheduledFutureMap.forEach((key, value) -> {
            ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(key);
            scheduledFuture.cancel(true);
            scheduledTaskMap.remove(key);
        });
    }

    @Override
    public void manual(String taskCode) {
        try {
            Task task = taskRepository.findById(taskCode);
            applicationContext.getBean(task.getClassName(), TaskService.class).execute();
        } catch (Exception e) {
            logger.error(String.format("手动执行任务[%s]失败", taskCode), e);
        }
    }

    private void doStartTask(Task task) {
        //获取需要定时调度的接口
        TaskService taskService = scheduledTaskMap.get(task.getCode());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(taskService, triggerContext -> new CronTrigger(task.getCron()).nextExecutionTime(triggerContext));
        //将启动的任务放入map
        scheduledFutureMap.put(task.getCode(), scheduledFuture);
    }

    /**
     * 任务是否已经启动
     */
    private boolean isStart(String taskKey) {
        if (scheduledFutureMap.containsKey(taskKey)) {
            return !scheduledFutureMap.get(taskKey).isCancelled();
        }
        return false;
    }

    private boolean addTask(Task task) {
        try {
            TaskService target = applicationContext.getBean(task.getClassName(), TaskService.class);
            scheduledTaskMap.put(task.getCode(), target);
        } catch (Exception e) {
            logger.error("添加任务失败 {}", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
