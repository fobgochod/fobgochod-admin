package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.service.file.FileOpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file/recycle")
public class FileRecycleController {

    @Autowired
    private FileOpService fileOpService;

    /**
     * 删除文件
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        if (body.getId() != null) {
            fileOpService.deleteRecycleBin(body.getId());
        }
        for (String recycleId : body.getRecycleIds()) {
            fileOpService.deleteRecycleBin(recycleId);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 恢复文件
     */
    @PostMapping("/restore")
    public ResponseEntity<?> recycleRestore(@RequestBody BatchFid body) {
        if (body.getId() != null) {
            fileOpService.restoreFile(body.getId());
        }
        for (String recycleId : body.getRecycleIds()) {
            fileOpService.restoreFile(recycleId);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 清空回收站
     */
    @PostMapping("/clear")
    public ResponseEntity<?> clear() {
        fileOpService.clearRecycleBin();
        return ResponseEntity.ok().build();
    }
}
