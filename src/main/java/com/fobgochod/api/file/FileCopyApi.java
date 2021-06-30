package com.fobgochod.api.file;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.annotation.FidCheck;
import com.fobgochod.domain.enumeration.FidType;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.service.business.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 复制
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileCopyApi {

    @Autowired
    private FileService fileService;

    /**
     * 复制文件
     *
     * @param bucket      存储区名称
     * @param fileId      文件ID
     * @param targetDirId 目标目录的ID
     * @return 复制文件ID
     */
    @PostMapping("/copy/{fileId}/{targetDirId}")
    public StdData copyFile(@PathVariable(required = false) String bucket,
                            @FidCheck @PathVariable String fileId,
                            @FidCheck(FidType.Directory) @PathVariable String targetDirId) {
        String copyId = fileService.copyFile(fileId, targetDirId);
        return StdData.ofSuccess(Collections.singletonMap("fileId", copyId));
    }

    /**
     * 复制目录
     *
     * @param bucket      存储区名称
     * @param dirId       目录ID
     * @param targetDirId 目标目录的ID
     * @return 复制目录ID
     */
    @PostMapping("/copy/dir/{dirId}/{targetDirId}")
    public StdData copyDir(@PathVariable(required = false) String bucket,
                           @FidCheck(FidType.Directory) @PathVariable String dirId,
                           @FidCheck(FidType.Directory) @PathVariable String targetDirId) {
        String copyId = fileService.copyDir(dirId, targetDirId);
        return StdData.ofSuccess(Collections.singletonMap("dirId", copyId));
    }

    /**
     * 批量复制
     * 将目录dirId复制到targetDirId下面
     *
     * @param bucket      存储区名称
     * @param bucket      文件ID、目录ID
     * @param targetDirId 目标目录的ID
     * @return 空
     */
    @PostMapping("/copy/{targetDirId}")
    public StdData copyFiles(@PathVariable(required = false) String bucket,
                             @RequestBody BatchFid body,
                             @FidCheck(FidType.Directory) @PathVariable String targetDirId) {
        body.afterPropertiesSet();
        return StdData.ofSuccess(fileService.batchCopy(bucket, body, targetDirId));
    }
}
