package com.fobgochod.service.file;

import com.fobgochod.entity.file.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件处理
 *
 * @author seven
 * @date 2021/2/28
 */
public interface UploadService {

    /**
     * 把指定流作为文件内容上传
     *
     * @param fileInfo 文件信息
     * @param source   数据
     * @return 文件信息
     */
    FileInfo uploadFile(FileInfo fileInfo, InputStream source);

    /**
     * 上传文件
     *
     * @param fileInfo 文件信息
     * @param file     文件
     */
    FileInfo uploadFile(FileInfo fileInfo, MultipartFile file);

    /**
     * 删除文件
     */
    void deleteFile(FileInfo fileInfo);
}
