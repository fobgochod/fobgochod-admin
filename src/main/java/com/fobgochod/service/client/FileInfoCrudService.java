package com.fobgochod.service.client;

import com.fobgochod.service.client.base.EntityService;
import com.fobgochod.domain.RecentFile;
import com.fobgochod.domain.select.ImageOption;
import com.fobgochod.domain.v2.FileTree;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.domain.v2.PageData;
import com.fobgochod.entity.file.FileInfo;
import org.bson.conversions.Bson;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface FileInfoCrudService extends EntityService<FileInfo> {

    List<FileInfo> findLtModifyDate(LocalDateTime modifyDate);

    List<FileInfo> findGtModifyDate(LocalDateTime modifyDate);

    List<FileInfo> getFileInfoUncompleted();

    List<RecentFile> getFileInfoRecently();

    List<FileInfo> findExpired();

    List<FileInfo> findByDirId(String directoryId);

    FileInfo findByDirIdAndName(String directoryId, String fileName);

    List<FileInfo> findByDirId(String dirId, Bson sort);

    long forceDeleteFile(LocalDateTime modifyDate);

    /**
     * 填充文件信息
     *
     * @param fileInfo
     * @return
     */
    FileInfo getFileInfo(FileInfo fileInfo);

    FileInfo getFileInfo(FileInfo fileInfo, MultipartFile file);

    FileTree getFileTree(String directoryId, String filePath);

    List<FileInfo> getFileInfo(Bson filter, Bson fields, Bson sort);

    List<FileInfo> getFileInfo(Bson filter, Bson fields, Bson sort, int pageNum, int pageSize);

    FileInfo getByFileName(String directoryId, String fileName);

    void changeFileName(String fileInfoId, String newFileName);

    PageData<ImageOption> getImageByPage(Page page);
}
