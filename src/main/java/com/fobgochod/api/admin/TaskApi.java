package com.fobgochod.api.admin;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task 定时任务
 *
 * @author zhouxiao
 * @date 2020/11/23
 */
@RestController
@RequestMapping("/tasks")
public class TaskApi {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Task body) {
        String id = taskRepository.insert(body);
        return ResponseEntity.ok(taskRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Task body) {
        taskRepository.update(body);
        return ResponseEntity.ok(taskRepository.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(taskRepository.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(taskRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        taskRepository.dropCollection();
        return ResponseEntity.ok().build();
    }
}
