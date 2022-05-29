package com.fobgochod.api.file;

import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.service.file.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private DownloadService downloadService;

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
