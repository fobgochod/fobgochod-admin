package com.fobgochod.service.file;

import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.RecycleBin;

/**
 * 文件处理
 *
 * @author seven
 * @date 2021/2/28
 */
public interface FileOpService {

    RecycleBin deleteFile(String fileInfoId);

    void deleteFileForce(String fileInfoId);

    RecycleBin deleteDir(String dirId);

    void deleteDirForce(String dirId);

    void deleteRecycleBin(String recycleBinId);

    void restoreFile(String recycleBinId);

    void clearRecycleBin();

    void checkFileBeforeUse(FileInfo fileInfo);

    FileInfo findFileInfo(String fileInfoId);
}
