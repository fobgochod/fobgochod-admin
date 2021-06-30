package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.service.client.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImageApi {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 获取所有图片预览地址
     *
     * @return
     */
    @PostMapping("/search")
    public StdData images(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(fileInfoCrudService.getImageByPage(body));
    }
}
