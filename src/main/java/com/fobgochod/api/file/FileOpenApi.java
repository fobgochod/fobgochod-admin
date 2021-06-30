package com.fobgochod.api.file;

import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.ShareCrudService;
import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.domain.enumeration.ShareType;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.domain.v2.FileTree;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.service.business.FileHandlerService;
import com.fobgochod.service.business.FileOpService;
import com.fobgochod.domain.StdData;
import com.fobgochod.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
public class FileOpenApi {

    @Autowired
    private FileOpService fileOpService;
    @Autowired
    private ShareCrudService shareCrudService;
    @Autowired
    private FileHandlerService fileHandlerService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    /**
     * 分享预览
     *
     * @param bucket 存储区
     * @param bucket 分享ID
     * @return
     */
    @GetMapping("/share/{shareId}")
    public StdData share(@PathVariable(required = false) String bucket,
                         @PathVariable String shareId,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        ShareRecord shareRecord = shareCrudService.findById(shareId);
        if (shareCrudService.isShareExpired(shareRecord)) {
            throw new BusinessException("分享链接已失效或者过期");
        }
        if (shareRecord.getType() == ShareType.One) {
            fileOpService.downloadFile(shareRecord.getFileId(), InlineAttachment.inline, request, response);
        }
        List<FileTree> fileTrees = fileHandlerService.getFileTrees(new BatchFid(shareRecord));
        return StdData.ofSuccess(fileTrees);
    }

    /**
     * 下载文件
     *
     * @param bucket
     * @param fileId
     * @return
     */
    @GetMapping("/download/2/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable(required = false) String bucket,
                                             @PathVariable String fileId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileId);
        byte[] bytes = fileOpService.downloadToBytes(fileId);
        Resource file = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileInfo.getName())
                .body(file);
    }

    /**
     * 预览文件
     *
     * @param bucket 存储区
     * @param bucket 文件ID
     * @return
     */
    @GetMapping("/preview/{fileId}")
    public void preview(@PathVariable(required = false) String bucket,
                        @PathVariable String fileId,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        fileOpService.downloadFile(fileId, InlineAttachment.inline, request, response);
    }
}
