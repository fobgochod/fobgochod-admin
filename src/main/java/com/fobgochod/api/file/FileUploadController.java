package com.fobgochod.api.file;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.file.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * 上传
 *
 * @author seven
 * @date 2021/3/9
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload/stream")
    public Callable<?> uploadStream(@RequestHeader(value = FghConstants.HTTP_HEADER_API_ARG_KEY) FileInfo body,
                                    HttpServletRequest request) {
        return () -> uploadService.uploadFile(body, request.getInputStream());
    }

    /**
     * 上传文件-单个文件
     *
     * @param file File
     * @param body 文件信息
     * @return 文件信息
     */
    @PostMapping("/upload")
    public Callable<?> upload(@RequestPart("file") MultipartFile file,
                              @RequestPart(required = false, name = "fileInfo") FileInfo body) throws Exception {
        return () -> uploadService.uploadFile(body, file);
    }

    /**
     * 上传文件-多个文件
     *
     * @param files File []
     * @param body  文件信息 []
     * @return 文件信息 []
     */
    @PostMapping("/upload/multi")
    public Callable<?> uploadMulti(@RequestPart("files") MultipartFile[] files,
                                   @RequestPart(required = false, name = "fileInfos") List<FileInfo> body) {
        return () -> {
            List<FileInfo> fileInfos = body == null ? new ArrayList<>() : body;
            int lackCount = files.length - fileInfos.size();
            if (lackCount > 0) {
                IntStream.range(0, lackCount).mapToObj(i -> new FileInfo()).forEach(fileInfos::add);
            }
            IntStream.range(0, files.length).forEach(i -> uploadService.uploadFile(fileInfos.get(i), files[i]));
            return fileInfos;
        };
    }
}
