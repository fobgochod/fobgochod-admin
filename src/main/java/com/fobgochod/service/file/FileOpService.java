package com.fobgochod.service.file;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.FileOpTree;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.RecycleBin;

import java.util.List;

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

    void moveFile(String fileInfoId, String directoryId);

    void moveDir(String dirId, String targetDirId);

    String copyFile(String fileInfoId, String directoryId);

    String copyDir(String dirId, String targetDirId);

    DirInfo dirOpCheck(String dirId);

    FileInfo fileOpCheck(String fileInfoId);

    List<FileOpTree> batchCopy(BatchFid batchFid, String targetDirId);

    void checkFileBeforeUse(FileInfo fileInfo);

    FileInfo findFileInfo(String fileInfoId);
}
