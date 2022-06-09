package com.fobgochod.api.fileinfo;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.Directory;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Fid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.crud.DirectoryCrudService;
import com.fobgochod.service.crud.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
public class DirInfoController {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @PostMapping("/add")
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

    @PostMapping("/del")
    public ResponseEntity<?> deleteById(@RequestBody DirInfo body) {
        directoryCrudService.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody DirInfo body) {
        directoryCrudService.update(body);
        return ResponseEntity.ok(directoryCrudService.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody DirInfo body) {
        return ResponseEntity.ok(directoryCrudService.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<DirInfo> body) {
        return ResponseEntity.ok(directoryCrudService.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(directoryCrudService.deleteByIdIn(body.getDirIds()));
    }

    @PostMapping("/info")
    public ResponseEntity<?> info(@RequestBody Fid body) {
        List<DirInfo> dirs = directoryCrudService.findByParentId(body.getId());
        List<FileInfo> files = fileInfoCrudService.findByDirId(body.getId());
        return ResponseEntity.ok(Directory.build(dirs, files));
    }
}
