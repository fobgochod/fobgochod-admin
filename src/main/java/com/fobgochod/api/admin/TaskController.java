package com.fobgochod.api.admin;

import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.select.Option;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Task 定时任务
 *
 * @author zhouxiao
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody Task body) {
        String id = taskRepository.insert(body);
        return ResponseEntity.ok(taskRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody Task body) {
        taskRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody Task body) {
        taskRepository.update(body);
        return ResponseEntity.ok(taskRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody Task body) {
        return ResponseEntity.ok(taskRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<Task> body) {
        return ResponseEntity.ok(taskRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        taskRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/option")
    public ResponseEntity<?> option() {
        List<Option> options = new ArrayList<>();
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(o -> options.add(new Option(o.getId(), o.getCode(), o.getName())));
        return ResponseEntity.ok(options);
    }
}
