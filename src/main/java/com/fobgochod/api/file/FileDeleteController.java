package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.service.file.FileOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 删除文件-指定文件-永久删除
     *
     * @param fileId 文件ID
     * @return 空
     */
    @DeleteMapping("/delete/force/{fileId}")
    public ResponseEntity<?> deleteForce(@PathVariable String fileId) {
        fileOpService.deleteFileForce(fileId);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除文件-多个文件、目录-永久删除
     *
     * @param body 文件ID、目录ID
     * @return 空
     */
    @PostMapping("/delete/force")
    public ResponseEntity<?> deleteForce(@RequestBody BatchFid body) {
        body.afterPropertiesSet();
        for (String fileId : body.getFileIds()) {
            fileOpService.deleteFileForce(fileId);
        }
        for (String dirId : body.getDirIds()) {
            fileOpService.deleteDirForce(dirId);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 删除文件-指定文件-进回收站
     *
     * @param fileId 文件ID
     * @return 回收站信息
     */
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> delete(@PathVariable String fileId) {
        RecycleBin recyclebin = fileOpService.deleteFile(fileId);
        return ResponseEntity.ok(recyclebin);
    }

    /**
     * 删除文件-多个文件、目录-进回收站
     *
     * @param body 文件ID、目录ID
     * @return 回收站信息 []
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        body.afterPropertiesSet();
        List<RecycleBin> recycleBins = new ArrayList<>();
        for (String fileId : body.getFileIds()) {
            RecycleBin recycleBin = fileOpService.deleteFile(fileId);
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
}
