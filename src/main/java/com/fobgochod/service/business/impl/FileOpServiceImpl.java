package com.fobgochod.service.business.impl;

import com.fobgochod.domain.enumeration.InlineAttachment;
import com.fobgochod.domain.enumeration.MimeType;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.service.business.FileOpService;
import com.fobgochod.service.business.FileService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.mongo.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 文件操作
 *
 * @author seven
 * @date 2021/3/8
 */
@Service
public class FileOpServiceImpl implements FileOpService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public void downloadToStream(FileInfo fileInfo, OutputStream out) {
        if (fileService.fileUnCompleted(fileInfo)) {
            throw new BusinessException("文件不存在或者正在上传中，不能下载！");
        }
        fileStorage.downloadToStream(fileInfo.getFileId(), out);
    }

    @Override
    public void downloadPartToStream(FileInfo fileInfo, OutputStream out, long start, long length) {
        if (fileService.fileUnCompleted(fileInfo)) {
            throw new BusinessException("文件不存在或者正在上传中，不能下载！");
        }
        fileStorage.downloadPartToStream(fileInfo.getFileId(), out, start, length);
    }

    @Override
    public byte[] downloadToBytes(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileService.fileUnCompleted(fileInfo)) {
            throw new BusinessException("文件不存在或者正在上传中，不能下载！");
        }
        String fileId = fileInfo.getFileId().toString();
        return fileStorage.downloadToBytes(fileId);
    }

    @Override
    public void downloadFile(String fileInfoId, InlineAttachment type, HttpServletRequest request, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileInfo == null) {
            throw new BusinessException("没有该文件：" + fileInfoId);
        } else {
            if (InlineAttachment.attachment.equals(type)) {
                // 操作记录，预览没有Token，不用记录
                fileInfoCrudService.update(fileInfo);
            }
        }
        if (MimeType.isVideo(fileInfo.getContentType())) {
            this.downloadFileRange(fileInfo, type, request, response);
        } else {
            this.downloadFile(fileInfoId, type, response);
        }
    }

    @Override
    public void downloadFile(String fileInfoId, InlineAttachment type, HttpServletResponse response) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileInfo == null) {
            throw new BusinessException("没有该文件");
        }
        byte[] fileBytes = this.downloadToBytes(fileInfoId);
        try {
            if (StringUtils.isEmpty(fileInfo.getContentType())) {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            } else {
                response.setContentType(fileInfo.getContentType());
            }
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileInfo.getSize()));
            String fileName = URLEncoder.encode(fileInfo.getName(), StandardCharsets.UTF_8.name());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("%s; filename=%s", type.name(), fileName));
            response.getOutputStream().write(fileBytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            try {
                if (response != null && response.getOutputStream() != null) {
                    response.getOutputStream().close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void downloadFilePart(String fileInfoId, HttpServletResponse response, long start, long length) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileInfo == null) {
            throw new BusinessException("没有该文件");
        } else {
            // 操作记录
            fileInfoCrudService.update(fileInfo);
        }
        try {
            if (StringUtils.isEmpty(fileInfo.getContentType())) {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            } else {
                response.setContentType(fileInfo.getContentType());
            }
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileInfo.getSize()));
            String fileName = URLEncoder.encode(fileInfo.getName(), StandardCharsets.UTF_8.name());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("%s; filename=%s", InlineAttachment.attachment.name(), fileName));
            this.downloadPartToStream(fileInfo, response.getOutputStream(), start, length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            try {
                if (response != null && response.getOutputStream() != null) {
                    response.getOutputStream().close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void downloadFileRange(FileInfo fileInfo, InlineAttachment type, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType(fileInfo.getContentType());
            response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            response.setHeader(HttpHeaders.ETAG, fileInfo.getName());
            response.setHeader(HttpHeaders.LAST_MODIFIED, new Date().toString());
            String fileName = URLEncoder.encode(fileInfo.getName(), StandardCharsets.UTF_8.name());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("%s; filename=%s", type.name(), fileName));

            long fileSize = fileInfo.getSize();
            long start = 0, end = 0, requestSize = fileSize;
            String range = request.getHeader(HttpHeaders.RANGE);
            if (range == null) {
                //第一次请求只返回Content-Length来让客户端请求多次实际数据
                response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            } else {
                if (range.startsWith("bytes=")) {
                    String[] values = range.split("=")[1].split("-");
                    start = Long.parseLong(values[0]);
                    if (values.length > 1) {
                        end = Long.parseLong(values[1]);
                    }
                }
                if (end != 0 && end > start) {
                    requestSize = end - start + 1;
                } else {
                    requestSize = fileInfo.getSize();
                }
                if (end > 0) {
                    response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(requestSize));
                } else {
                    response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + (fileSize - 1) + "/" + fileSize);
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(requestSize));
                }
                // 206 以后的多次以断点续传的方式来返回视频数据
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            }
            this.downloadPartToStream(fileInfo, response.getOutputStream(), start, requestSize);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            try {
                if (response != null && response.getOutputStream() != null) {
                    response.getOutputStream().close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
