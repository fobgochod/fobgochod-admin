package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.client.ShareCrudService;
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
 * SharedFile 文件分享
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/share")
public class ShareApi {

    @Autowired
    private ShareCrudService shareCrudService;

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public StdData delete(@PathVariable String id) {
        shareCrudService.deleteById(id);
        return StdData.ok();
    }

    /**
     * 修改
     *
     * @param body
     * @return
     */
    @PutMapping
    public StdData modify(@RequestBody ShareRecord body) {
        shareCrudService.update(body);
        return StdData.ofSuccess(shareCrudService.findById(body.getId()));
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public StdData findById(@PathVariable String id) {
        return StdData.ofSuccess(shareCrudService.findById(id));
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find() {
        return StdData.ofSuccess(shareCrudService.findAll());
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(shareCrudService.findByPage(body));
    }
}
