package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.client.DirectoryCrudService;
import com.fobgochod.service.client.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileInfo 文件信息
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/fileinfo")
public class FileInfoApi {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody FileInfo body) {
        FileInfo fileInfo = fileInfoCrudService.findById(body.getId());
        if (fileInfo == null) {
            throw new SystemException("文件不存在" + body.getId());
        }
        if (!directoryCrudService.exists(body.getDirectoryId())) {
            throw new SystemException("目录不存在" + body.getDirectoryId());
        }
        if (body.getName() != null) {
            fileInfo.setName(body.getName());
        }
        fileInfo.setDisplayName(body.getDisplayName());
        fileInfo.setTag(body.getTag());
        if (body.getExtension() != null) {
            fileInfo.setExtension(body.getExtension());
        }
        if (body.getContentType() != null) {
            fileInfo.setContentType(body.getContentType());
        }
        fileInfo.setExpireDate(body.getExpireDate());
        fileInfo.setDirectoryId(body.getDirectoryId());
        fileInfo.setMetadata(body.getMetadata());
        fileInfo.setTenantId(body.getTenantId());
        fileInfoCrudService.update(fileInfo);
        return ResponseEntity.ok(fileInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(fileInfoCrudService.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(fileInfoCrudService.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> drop() {
        fileInfoCrudService.dropCollection();
        return ResponseEntity.ok().build();
    }

    /**
     * 更新文件状态
     *
     * @param id 文件id
     */
    @PostMapping("/{id}/{completed}")
    public ResponseEntity<?> modify(@PathVariable String id,
                                    @PathVariable boolean completed) {
        FileInfo fileInfo = fileInfoCrudService.findById(id);
        fileInfo.setCompleted(completed);
        fileInfoCrudService.update(fileInfo);
        return ResponseEntity.ok(fileInfo);
    }

    /**
     * 修改文件名
     */
    @PostMapping("/name/{id}")
    public ResponseEntity<?> changeName(@PathVariable String id,
                                        @RequestBody FileInfo body) {
        FileInfo fileInfo = fileInfoCrudService.findById(id);
        fileInfo.setName(body.getName());
        fileInfoCrudService.update(fileInfo);
        return ResponseEntity.ok(fileInfoCrudService.findById(id));
    }
}
