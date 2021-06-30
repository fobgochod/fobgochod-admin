package com.fobgochod.api.admin;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.admin.Task;
import com.fobgochod.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 新增
     *
     * @param body
     * @return
     */
    @PostMapping
    public StdData create(@RequestBody Task body) {
        String id = taskRepository.insert(body);
        return StdData.ofSuccess(taskRepository.findById(id));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public StdData delete(@PathVariable String id) {
        taskRepository.deleteById(id);
        return StdData.ok();
    }

    /**
     * 修改
     *
     * @param body
     * @return
     */
    @PutMapping
    public StdData modify(@RequestBody Task body) {
        taskRepository.update(body);
        return StdData.ofSuccess(taskRepository.findById(body.getId()));
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public StdData findById(@PathVariable String id) {
        return StdData.ofSuccess(taskRepository.findById(id));
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find(@RequestBody(required = false) Task body) {
        return StdData.ofSuccess(taskRepository.findAll(body));
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(taskRepository.findByPage(body));
    }

    /**
     * 删除表
     *
     * @return
     */
    @DeleteMapping("/drop")
    public StdData dropCollection() {
        taskRepository.dropCollection();
        return StdData.ok();
    }
}
