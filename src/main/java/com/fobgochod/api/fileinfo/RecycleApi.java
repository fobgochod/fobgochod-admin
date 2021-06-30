package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.service.client.RecycleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RecycleBin 文件回收站
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/recycle")
public class RecycleApi {

    @Autowired
    private RecycleCrudService recycleCrudService;

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find() {
        return StdData.ofSuccess(recycleCrudService.findAll());
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(recycleCrudService.findByPage(body));
    }
}
