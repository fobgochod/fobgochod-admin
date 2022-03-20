package com.fobgochod.api;

import com.fobgochod.service.schedule.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class TaskManagerController {

    @Autowired
    private TaskManager taskManager;

    @GetMapping("/start/{code}")
    public ResponseEntity<?> start(@PathVariable String code) {
        return ResponseEntity.ok(taskManager.start(code));
    }

    @GetMapping("/stop/{code}")
    public ResponseEntity<?> stop(@PathVariable String code) {
        return ResponseEntity.ok(taskManager.stop(code));
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh() {
        taskManager.refresh();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shutdown")
    public ResponseEntity<?> shutdown() {
        taskManager.refresh();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/run/{code}")
    public ResponseEntity<?> run(@PathVariable String code) {
        taskManager.manual(code);
        return ResponseEntity.ok().build();
    }
}
