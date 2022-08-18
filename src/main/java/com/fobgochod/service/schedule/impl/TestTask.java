package com.fobgochod.service.schedule.impl;

import com.fobgochod.service.schedule.TaskIdEnum;
import com.fobgochod.service.schedule.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author zhouxiao
 * @date 2021/1/26
 * @see TaskIdEnum#TS001
 */
@Component
public class TestTask extends TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);
    private int count;

    @Override
    public void execute() {
        logger.error("Thread id:{}, TestTask execute times:{}", Thread.currentThread().getId(), ++count);
    }
}
