package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.file.ShrinkImage;
import com.fobgochod.service.crud.ShrinkCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class ShrinkController {

    @Autowired
    private ShrinkCrudService shrinkCrudService;

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody ShrinkImage body) {
        shrinkCrudService.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<ShrinkImage> body) {
        return ResponseEntity.ok(shrinkCrudService.findByPage(body));
    }
}
