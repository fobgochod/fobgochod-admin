package com.fobgochod.api.admin;

import com.fobgochod.domain.StdData;
import com.fobgochod.service.schedule.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task 定时任务管理
 *
 * @author zhouxiao
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/tasks")
public class TaskManagerApi {

    @Autowired
    private TaskManager taskManager;

    @GetMapping("/start/{code}")
    public StdData start(@PathVariable String code) {
        return StdData.ofSuccess(taskManager.start(code));
    }

    @GetMapping("/stop/{code}")
    public StdData stop(@PathVariable String code) {
        return StdData.ofSuccess(taskManager.stop(code));
    }

    @GetMapping("/refresh")
    public StdData refresh() {
        taskManager.refresh();
        return StdData.ok();
    }

    @GetMapping("/shutdown")
    public StdData shutdown() {
        taskManager.refresh();
        return StdData.ok();
    }

    @GetMapping("/run/{code}")
    public StdData run(@PathVariable String code) {
        taskManager.manual(code);
        return StdData.ok();
    }
}
