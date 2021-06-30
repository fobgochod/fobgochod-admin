package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.service.client.DirectoryCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Directory 目录
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/directory")
public class DirInfoApi {

    @Autowired
    private DirectoryCrudService directoryCrudService;

    /**
     * 新增
     *
     * @param body
     * @return
     */
    @PostMapping
    public StdData create(@RequestBody DirInfo body) {
        body.setParentId(body.getParentId());
        String id = directoryCrudService.insert(body);
        return StdData.ofSuccess(directoryCrudService.findById(id));
    }

    /**
     * 修改
     *
     * @param body
     * @return
     */
    @PutMapping
    public StdData modify(@RequestBody DirInfo body) {
        directoryCrudService.update(body);
        return StdData.ofSuccess(directoryCrudService.findById(body.getId()));
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public StdData findById(@PathVariable String id) {
        return StdData.ofSuccess(directoryCrudService.findById(id));
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find() {
        return StdData.ofSuccess(directoryCrudService.findAll());
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(directoryCrudService.findByPage(body));
    }
}
