package com.fobgochod.api.file;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.annotation.FidCheck;
import com.fobgochod.domain.enumeration.FidType;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.service.business.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/file")
public class FileRecycleApi {

    @Autowired
    private FileService fileService;

    /**
     * 回收站-删除文件
     *
     * @param bucket    存储区名称
     * @param recycleId 垃圾箱内容id
     * @return 空
     */
    @DeleteMapping("/recycle/delete/{recycleId}")
    public StdData recycleDelete(@PathVariable(required = false) String bucket,
                                 @FidCheck(FidType.Recycle) @PathVariable String recycleId) {
        fileService.deleteRecycleBin(recycleId);
        return StdData.ok();
    }

    /**
     * 回收站-清空
     *
     * @param bucket 存储区名称
     * @return 空
     */
    @DeleteMapping("/recycle/clear")
    public StdData recycleClear(@PathVariable(required = false) String bucket) {
        fileService.clearRecycleBin();
        return StdData.ok();
    }

    /**
     * 回收站-恢复指定文件
     *
     * @param bucket    存储区
     * @param recycleId 垃圾箱内容ID
     * @return 空
     */
    @PostMapping("/recycle/restore/{recycleId}")
    public StdData recycleRestore(@PathVariable(required = false) String bucket,
                                  @FidCheck(FidType.Recycle) @PathVariable String recycleId) {
        fileService.restoreFile(recycleId);
        return StdData.ok();
    }

    /**
     * 回收站-恢复选择文件
     *
     * @param bucket 存储区
     * @return 空
     */
    @PostMapping("/recycle/restore")
    public StdData recycleRestore(@PathVariable(required = false) String bucket,
                                  @RequestBody BatchFid body) {
        for (String recycleId : body.getRecycleIds()) {
            fileService.restoreFile(recycleId);
        }
        return StdData.ok();
    }
}
