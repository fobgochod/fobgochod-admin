package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.service.crud.RecycleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class RecycleController {

    @Autowired
    private RecycleCrudService recycleCrudService;

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<RecycleBin> body) {
        return ResponseEntity.ok(recycleCrudService.findByPage(body));
    }
}
