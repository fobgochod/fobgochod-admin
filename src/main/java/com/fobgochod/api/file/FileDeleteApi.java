package com.fobgochod.api.file;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.annotation.FidCheck;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.service.business.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FileDeleteApi {

    @Autowired
    private FileService fileService;

    /**
     * 删除文件-指定文件-永久删除
     *
     * @param fileId 文件ID
     * @return 空
     */
    @DeleteMapping("/delete/force/{fileId}")
    public StdData deleteForce(@FidCheck @PathVariable String fileId) {
        fileService.deleteFile(fileId, true);
        return StdData.ok();
    }

    /**
     * 删除文件-多个文件、目录-永久删除
     *
     * @param body 文件ID、目录ID
     * @return 空
     */
    @PostMapping("/delete/force")
    public StdData deleteForce(@RequestBody BatchFid body) {
        body.afterPropertiesSet();
        for (String fileId : body.getFileIds()) {
            fileService.deleteFile(fileId, true);
        }
        for (String dirId : body.getDirIds()) {
            fileService.deleteDir(dirId, true);
        }
        return StdData.ok();
    }

    /**
     * 删除文件-指定文件-进回收站
     *
     * @param fileId 文件ID
     * @return 回收站信息
     */
    @DeleteMapping("/delete/{fileId}")
    public StdData delete(@PathVariable String fileId) {
        RecycleBin recyclebin = fileService.deleteFile(fileId);
        return StdData.ofSuccess(recyclebin);
    }

    /**
     * 删除文件-多个文件、目录-进回收站
     *
     * @param body 文件ID、目录ID
     * @return 回收站信息 []
     */
    @PostMapping("/delete")
    public StdData delete(@RequestBody BatchFid body) {
        body.afterPropertiesSet();
        List<RecycleBin> recycleBins = new ArrayList<>();
        for (String fileId : body.getFileIds()) {
            RecycleBin recycleBin = fileService.deleteFile(fileId);
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        for (String dirId : body.getDirIds()) {
            RecycleBin recycleBin = fileService.deleteDir(dirId);
            if (recycleBin != null) {
                recycleBins.add(recycleBin);
            }
        }
        return StdData.ofSuccess(recycleBins);
    }
}
