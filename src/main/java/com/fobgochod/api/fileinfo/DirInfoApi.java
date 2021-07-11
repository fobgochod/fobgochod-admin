package com.fobgochod.api.fileinfo;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.Directory;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
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

import java.util.List;

/**
 * Directory 目录
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/directory")
public class DirInfoApi {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DirInfo body) {
        DirInfo parent = directoryCrudService.findById(body.getParentId());
        if (parent != null) {
            body.setParentId(parent.getId());
        } else {
            body.setParentId(BaseField.ROOT_DIR);
        }
        String id = directoryCrudService.insert(body);
        return ResponseEntity.ok(directoryCrudService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        directoryCrudService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody DirInfo body) {
        directoryCrudService.update(body);
        return ResponseEntity.ok(directoryCrudService.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(directoryCrudService.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(directoryCrudService.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(directoryCrudService.deleteByIdIn(body.getDirIds()));
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> list(@PathVariable String id) {
        List<DirInfo> dirs = directoryCrudService.findByParentId(id);
        List<FileInfo> files = fileInfoCrudService.findByDirId(id);
        return ResponseEntity.ok(Directory.build(dirs, files));
    }
}
