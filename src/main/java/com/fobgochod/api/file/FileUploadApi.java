package com.fobgochod.api.file;

import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.business.FileService;
import com.fobgochod.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 上传
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileUploadApi {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件-单个文件
     *
     * @param file File
     * @param body 文件信息
     * @return 文件信息
     */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file,
                                    @RequestPart(required = false, name = "fileInfo") FileInfo body) {
        Authentication authentication = UserUtil.getAuthentication();
        CompletableFuture<FileInfo> future = CompletableFuture.supplyAsync(() ->
                fileService.uploadFile(body, file, authentication));
        try {
            return ResponseEntity.ok(future.get());
        } catch (Exception e) {
            throw new SystemException("上传失败" + e.getMessage());
        }
    }

    /**
     * 上传文件-多个文件
     *
     * @param files File []
     * @param body  文件信息 []
     * @return 文件信息 []
     */
    @PostMapping("/upload/multi")
    public ResponseEntity<?> uploadMulti(@RequestPart("files") MultipartFile[] files,
                                         @RequestPart(required = false, name = "fileInfos") Queue<FileInfo> body) {
        List<CompletableFuture<FileInfo>> futures = new ArrayList<>();
        Authentication authentication = UserUtil.getAuthentication();
        for (MultipartFile file : files) {
            CompletableFuture<FileInfo> future = CompletableFuture.supplyAsync(() ->
                    fileService.uploadFile(body == null ? null : body.poll(), file, authentication));
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[files.length])).join();
        return ResponseEntity.ok(futures.stream().map(fileInfo -> {
            try {
                return fileInfo.get();
            } catch (Exception e) {
                throw new SystemException(String.format("获取上传文件ID失败 %s", e.getMessage()));
            }
        }).collect(Collectors.toList()));
    }
}
