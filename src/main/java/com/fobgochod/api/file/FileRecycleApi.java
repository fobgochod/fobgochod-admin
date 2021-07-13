package com.fobgochod.api.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.service.business.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class FileRecycleApi {

    @Autowired
    private FileService fileService;

    /**
     * 回收站-删除文件
     *
     * @param recycleId 垃圾箱内容id
     * @return 空
     */
    @DeleteMapping("/delete/{recycleId}")
    public ResponseEntity<?> delete(@PathVariable String recycleId) {
        fileService.deleteRecycleBin(recycleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 回收站-清空
     *
     * @return 空
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clear() {
        fileService.clearRecycleBin();
        return ResponseEntity.ok().build();
    }

    /**
     * 回收站-恢复指定文件
     *
     * @param recycleId 垃圾箱内容ID
     * @return 空
     */
    @PostMapping("/restore/{recycleId}")
    public ResponseEntity<?> recycleRestore(@PathVariable String recycleId) {
        fileService.restoreFile(recycleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 回收站-恢复选择文件
     *
     * @return 空
     */
    @PostMapping("/restore")
    public ResponseEntity<?> recycleRestore(@RequestBody BatchFid body) {
        for (String recycleId : body.getRecycleIds()) {
            fileService.restoreFile(recycleId);
        }
        return ResponseEntity.ok().build();
    }
}
