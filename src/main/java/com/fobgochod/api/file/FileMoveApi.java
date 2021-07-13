package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.service.business.FileService;
import com.fobgochod.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 剪切
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileMoveApi {

    @Autowired
    private FileService fileService;

    /**
     * 剪贴文件
     *
     * @param fileId      文件ID
     * @param targetDirId 目的目录的ID
     * @return 空
     */
    @PostMapping("/move/{fileId}/{targetDirId}")
    public ResponseEntity<?> moveFile(@PathVariable String fileId,
                                   @PathVariable String targetDirId) {
        fileService.moveFile(fileId, IdUtil.getDirId(targetDirId));
        return ResponseEntity.ok().build();
    }

    /**
     * 剪贴目录
     * 将目录dirId移动到targetDirId下面
     *
     * @param dirId       目录ID
     * @param targetDirId 目的目录的ID
     * @return 是否成功
     */
    @PostMapping("/move/dir/{dirId}/{targetDirId}")
    public ResponseEntity<?> moveDir(@PathVariable String dirId,
                                  @PathVariable String targetDirId) {
        fileService.moveDir(dirId, targetDirId);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量剪切
     *
     * @param body        文件ID、目录ID
     * @param targetDirId 目的目录的ID
     * @return 是否成功
     */
    @PostMapping("/move/{targetDirId}")
    public ResponseEntity<?> moveManyFileInfo(@RequestBody BatchFid body,
                                           @PathVariable String targetDirId) {
        body.afterPropertiesSet();
        for (String fileId : body.getFileIds()) {
            fileService.moveFile(fileId, targetDirId);
        }
        for (String dirId : body.getDirIds()) {
            fileService.moveDir(dirId, targetDirId);
        }
        return ResponseEntity.ok().build();
    }
}
