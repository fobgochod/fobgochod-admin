package com.fobgochod.api.file;

import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.FileTree;
import com.fobgochod.service.business.FileHandlerService;
import com.fobgochod.service.business.FileOpService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class FileDownloadApi {

    @Autowired
    private FileOpService fileOpService;
    @Autowired
    private FileHandlerService fileHandlerService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 文件下载-指定文件
     *
     * @param bucket 存储区名称
     * @param fileId 文件ID
     * @return 文件流
     */
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable(required = false) String bucket,
                         @PathVariable String fileId,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        fileOpService.downloadFile(fileId, InlineAttachment.attachment, request, response);
    }

    /**
     * 文件下载-指定目录
     *
     * @param dirId
     * @param response
     */
    @GetMapping("/download/dir/{dirId}")
    public void downloadDir(@PathVariable String dirId,
                            HttpServletResponse response) {
        FileTree dirTree = fileInfoCrudService.getFileTree(IdUtil.getDirId(dirId), null);
        List<FileTree> fileTrees = Collections.singletonList(dirTree);
        String fileName = fileHandlerService.getFileName(fileTrees);
        fileHandlerService.compressFile(fileTrees, fileName, response);
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
        List<FileTree> fileTrees = fileHandlerService.getFileTrees(body);
        String fileName = fileHandlerService.getFileName(fileTrees);
        fileHandlerService.compressFile(fileTrees, fileName, response);
    }
}
