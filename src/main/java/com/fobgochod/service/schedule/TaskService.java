package com.fobgochod.service.schedule;

import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

public abstract class TaskService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private static final Integer SLEEP_MILLIS = 10 * 1000;

    @Autowired
    private TaskRepository taskRepository;

    protected String getBeanName() {
        String shortName = ClassUtils.getShortName(this.getClass());
        return Introspector.decapitalize(shortName);
    }

    /**
     * 任务执行
     */
    public abstract void execute() throws Exception;

    @Override
    public void run() {
        Task task = taskRepository.findByClassName(this.getBeanName());
        if (task == null) {
            logger.error("通过[{}]未找到任务", this.getBeanName());
            return;
        }
        boolean lock = taskRepository.lock(task.getId());
        if (lock) {
            try {
                if (task.getDisable()) {
                    logger.error("定时任务[{}], cron=[{}] 禁用", task.getCode(), task.getCron());
                    return;
                }
                this.execute();
                logger.info("定时任务[{}], cron=[{}] 执行成功", task.getCode(), task.getCron());
                Thread.sleep(SLEEP_MILLIS);
            } catch (Exception e) {
                logger.info("定时任务[{}], cron=[{}] 执行异常 {}", task.getCode(), task.getCron(), e.getMessage());
            } finally {
                taskRepository.unlock(task.getId());
            }
        }
    }
}
