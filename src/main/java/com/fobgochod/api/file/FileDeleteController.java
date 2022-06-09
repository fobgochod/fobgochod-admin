package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.service.file.FileOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileDeleteController {

    @Autowired
    private FileOpService fileOpService;

    /**
     * 删除文件
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        List<RecycleBin> recycleBins = new ArrayList<>();
        if (body.getId() != null) {
            RecycleBin recycleBin = fileOpService.deleteFile(body.getId());
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        for (String fileId : body.getFileIds()) {
            RecycleBin recycleBin = fileOpService.deleteFile(fileId);
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        return ResponseEntity.ok(recycleBins);
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete/force")
    public ResponseEntity<?> deleteForce(@RequestBody BatchFid body) {
        if (body.getId() != null) {
            fileOpService.deleteFileForce(body.getId());
        }
        for (String fileId : body.getFileIds()) {
            fileOpService.deleteFileForce(fileId);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 删除目录
     */
    @PostMapping("/delete/dir")
    public ResponseEntity<?> deleteDir(@RequestBody BatchFid body) {
        List<RecycleBin> recycleBins = new ArrayList<>();
        if (body.getId() != null) {
            RecycleBin recycleBin = fileOpService.deleteDir(body.getId());
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        for (String dirId : body.getDirIds()) {
            RecycleBin recycleBin = fileOpService.deleteDir(dirId);
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        return ResponseEntity.ok(recycleBins);
    }

    /**
     * 删除目录
     */
    @PostMapping("/delete/dir/force")
    public ResponseEntity<?> deleteDirForce(@RequestBody BatchFid body) {
        if (body.getId() != null) {
            fileOpService.deleteDirForce(body.getId());
        }
        for (String dirId : body.getDirIds()) {
            fileOpService.deleteDirForce(dirId);
        }
        return ResponseEntity.ok().build();
    }
}
