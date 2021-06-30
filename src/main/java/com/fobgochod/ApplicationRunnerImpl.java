package com.fobgochod;

import com.fobgochod.service.schedule.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 1)
public class ApplicationRunnerImpl implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunnerImpl.class);

    @Autowired
    private TaskManager taskManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskManager.refresh();
        logger.info("task init success!");
    }
}
