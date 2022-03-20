package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.service.file.FileOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 复制
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileCopyController {

    @Autowired
    private FileOpService fileOpService;

    /**
     * 复制文件
     *
     * @param fileId      文件ID
     * @param targetDirId 目标目录的ID
     * @return 复制文件ID
     */
    @PostMapping("/copy/{fileId}/{targetDirId}")
    public ResponseEntity<?> copyFile(@PathVariable String fileId,
                                      @PathVariable String targetDirId) {
        String copyId = fileOpService.copyFile(fileId, targetDirId);
        return ResponseEntity.ok(Collections.singletonMap("fileId", copyId));
    }

    /**
     * 复制目录
     *
     * @param dirId       目录ID
     * @param targetDirId 目标目录的ID
     * @return 复制目录ID
     */
    @PostMapping("/copy/dir/{dirId}/{targetDirId}")
    public ResponseEntity<?> copyDir(@PathVariable String dirId,
                                     @PathVariable String targetDirId) {
        String copyId = fileOpService.copyDir(dirId, targetDirId);
        return ResponseEntity.ok(Collections.singletonMap("dirId", copyId));
    }

    /**
     * 批量复制
     * 将目录dirId复制到targetDirId下面
     *
     * @param body        文件ID、目录ID
     * @param targetDirId 目标目录的ID
     * @return 空
     */
    @PostMapping("/copy/{targetDirId}")
    public ResponseEntity<?> copyFiles(@RequestBody BatchFid body,
                                       @PathVariable String targetDirId) {
        body.afterPropertiesSet();
        return ResponseEntity.ok(fileOpService.batchCopy(body, targetDirId));
    }
}