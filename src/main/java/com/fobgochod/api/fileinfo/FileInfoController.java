package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.crud.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * FileInfo 文件信息
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/fileinfo")
public class FileInfoController {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody FileInfo body) {
        fileInfoCrudService.update(body);
        return ResponseEntity.ok(fileInfoCrudService.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody FileInfo body) {
        return ResponseEntity.ok(fileInfoCrudService.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<FileInfo> body) {
        return ResponseEntity.ok(fileInfoCrudService.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> drop() {
        fileInfoCrudService.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change/name")
    public ResponseEntity<?> changeName(@RequestBody FileInfo body) {
        FileInfo fileInfo = fileInfoCrudService.findById(body.getId());
        fileInfo.setName(body.getName());
        fileInfoCrudService.update(fileInfo);
        return ResponseEntity.ok(fileInfo);
    }
}
