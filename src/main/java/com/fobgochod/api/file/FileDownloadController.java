package com.fobgochod.api.file;

import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.file.CompressService;
import com.fobgochod.service.file.DownloadService;
import com.fobgochod.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 下载
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileDownloadController {

    @Autowired
    private CompressService compressService;
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 预览文件
     *
     * @param fileId 文件ID
     */
    @GetMapping("/preview")
    public void preview(@RequestParam String fileId,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        downloadService.downloadFile(fileId, InlineAttachment.inline, request, response);
    }

    /**
     * 文件下载-指定文件
     *
     * @param fileId 文件ID
     */
    @GetMapping("/download")
    public void download(@RequestParam String fileId,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        downloadService.downloadFile(fileId, InlineAttachment.attachment, request, response);
    }

    /**
     * 文件下载-指定目录
     *
     * @param dirId 目录ID
     */
    @GetMapping("/download/dir")
    public void downloadDir(@RequestParam String dirId,
                            HttpServletResponse response) {
        FileTree dirTree = fileInfoCrudService.getFileTree(IdUtil.getDirId(dirId), null);
        List<FileTree> fileTrees = Collections.singletonList(dirTree);
        String fileName = compressService.getFileName(fileTrees);
        compressService.compressFile(fileTrees, fileName, response);
    }

    /**
     * 文件下载-多个文件、目录
     *
     * @param body     文件ID、目录ID
     * @param response 文件流
     */
    @PostMapping("/download/multi")
    public void downloadMulti(@RequestBody BatchFid body,
                              HttpServletResponse response) {
        List<FileTree> fileTrees = compressService.getFileTrees(body);
        String fileName = compressService.getFileName(fileTrees);
        compressService.compressFile(fileTrees, fileName, response);
    }
}
