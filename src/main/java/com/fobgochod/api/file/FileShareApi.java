package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.client.ShareCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分享
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file/share")
public class FileShareApi {

    @Autowired
    private ShareCrudService shareCrudService;

    /**
     * 文件分享-分享文件、文件夹
     *
     * @param body 文件ID、目录ID
     * @return 分享地址
     */
    @PostMapping
    public ResponseEntity<?> share(@RequestBody BatchFid body) {
        body.afterPropertiesSet();
        ShareRecord shareRecord = shareCrudService.shareFile(body);
        return ResponseEntity.ok(shareRecord);
    }
}
