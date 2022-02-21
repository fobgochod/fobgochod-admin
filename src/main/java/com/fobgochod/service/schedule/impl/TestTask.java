package com.fobgochod.service.schedule.impl;

import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author zhouxiao
 * @date 2021/1/26
 */
@Component
public class TestTask extends TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);
    private int count;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void execute() {
        Task task = taskRepository.findValidTaskByCode(TaskIdEnum.TS001.name());
        if (task != null) {
            logger.error("Thread id:{}, TestTask execute times:{}", Thread.currentThread().getId(), ++count);
        }
    }
}
