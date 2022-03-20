package com.fobgochod.api.file;

import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.domain.enumeration.ShareType;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.file.DownloadService;
import com.fobgochod.service.file.CompressService;
import com.fobgochod.service.crud.ShareCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 白名单，不需要权限
 *
 * @author seven
 * @date 2021/3/8
 */
@RestController
@RequestMapping("/file")
public class FileOpenController {

    @Autowired
    private CompressService compressService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private ShareCrudService shareCrudService;

    /**
     * 分享预览
     *
     * @param shareId 分享ID
     */
    @GetMapping("/share/{shareId}")
    public void share(@PathVariable String shareId, HttpServletRequest request, HttpServletResponse response) {
        ShareRecord shareRecord = shareCrudService.getShareUnExpired(shareId);
        if (shareRecord.getType() == ShareType.One) {
            downloadService.downloadFile(shareRecord.getFileId(), InlineAttachment.inline, request, response);
        } else {
            List<FileTree> fileTrees = compressService.getFileTrees(new BatchFid(shareRecord));
            String fileName = compressService.getFileName(fileTrees);
            compressService.compressFile(fileTrees, fileName, response);
        }
    }

    /**
     * 预览文件
     *
     * @param fileId 文件ID
     */
    @GetMapping("/preview/{fileId}")
    public void preview(@PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) {
        downloadService.downloadFile(fileId, InlineAttachment.inline, request, response);
    }
}
