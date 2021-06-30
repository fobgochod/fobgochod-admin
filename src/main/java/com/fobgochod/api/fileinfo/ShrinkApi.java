package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.service.client.ShrinkCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ImageShrink 图片压缩
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/shrink")
public class ShrinkApi {

    @Autowired
    private ShrinkCrudService shrinkCrudService;

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public StdData delete(@PathVariable String id) {
        shrinkCrudService.deleteById(id);
        return StdData.ok();
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find() {
        return StdData.ofSuccess(shrinkCrudService.findAll());
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(shrinkCrudService.findByPage(body));
    }
}
