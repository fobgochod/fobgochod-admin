package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.base.Page;
import com.fobgochod.service.crud.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片
 *
 * @author seven
 * @date 2021/3/8
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 获取所有图片预览地址
     *
     * @return
     */
    @PostMapping("/search")
    public ResponseEntity<?> images(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(fileInfoCrudService.getImageByPage(body));
    }
}
