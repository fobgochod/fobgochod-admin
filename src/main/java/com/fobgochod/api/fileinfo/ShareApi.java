package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.client.ShareCrudService;
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
 * SharedFile 文件分享
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/share")
public class ShareApi {

    @Autowired
    private ShareCrudService shareCrudService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        shareCrudService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody ShareRecord body) {
        shareCrudService.update(body);
        return ResponseEntity.ok(shareCrudService.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(shareCrudService.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(shareCrudService.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(shareCrudService.deleteByIdIn(body.getShareIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> drop() {
        shareCrudService.dropCollection();
        return ResponseEntity.ok().build();
    }
}
