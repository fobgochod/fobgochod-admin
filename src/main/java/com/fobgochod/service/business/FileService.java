package com.fobgochod.service.business;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.FileOpTree;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.RecycleBin;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件处理
 *
 * @author seven
 * @date 2021/2/28
 */
public interface FileService {


    /**
     * 把指定流作为文件内容上传
     *
     * @param fileInfo 文件信息
     * @param source   数据
     * @return 上传文件的id
     */
    String uploadFileStream(FileInfo fileInfo, InputStream source);

    FileInfo uploadFile(FileInfo fileInfo, InputStream source);

    FileInfo uploadFile(FileInfo fileInfo, InputStream source, Authentication authentication);

    /**
     * 上传文件
     *
     * @param fileInfo 文件信息
     * @param file     文件
     * @return
     */
    FileInfo uploadFile(FileInfo fileInfo, MultipartFile file);

    FileInfo uploadFile(FileInfo fileInfo, MultipartFile file, Authentication authentication);

    /**
     * 引用计数加一，减少同样的文件重复存储
     *
     * @param fileId 文件Id
     */
    void incrReferenceCount(ObjectId fileId);

    /**
     * 引用计数减一，当为0时，删除文件
     *
     * @param fileId 文件Id
     */
    void decrReferenceCount(ObjectId fileId);

    RecycleBin deleteFile(String fileInfoId);

    RecycleBin deleteFile(String fileInfoId, boolean force);

    RecycleBin deleteDir(String dirId);

    RecycleBin deleteDir(String dirId, boolean force);

    void deleteRecycleBin(String recycleBinId);

    void restoreFile(String recycleBinId);

    void clearRecycleBin();

    void moveFile(String fileInfoId, String directoryId);

    void moveDir(String dirId, String targetDirId);

    String copyFile(String fileInfoId, String directoryId);

    String copyDir(String dirId, String targetDirId);

    DirInfo dirOpCheck(String dirId);

    FileInfo fileOpCheck(String fileInfoId);

    List<FileOpTree> batchCopy(BatchFid batchFid, String targetDirId);

    boolean fileUnCompleted(FileInfo fileInfo);
}
