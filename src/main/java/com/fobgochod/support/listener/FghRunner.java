package com.fobgochod.support.listener;

import com.fobgochod.service.schedule.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FghRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(FghRunner.class);

    @Autowired
    private TaskManager taskManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskManager.refresh();
        logger.info("task init success!");
    }
}
