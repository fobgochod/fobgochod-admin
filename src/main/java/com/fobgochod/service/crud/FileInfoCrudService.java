package com.fobgochod.service.crud;

import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.StatsResult;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.domain.select.ImageOption;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.crud.base.EntityService;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoCrudService extends EntityService<FileInfo> {

    List<FileInfo> findByDirId(String directoryId);

    FileInfo findByDirIdAndName(String directoryId, String fileName);

    boolean needDelete(String id, ObjectId fileId);

    void fillFileInfo(FileInfo fileInfo);

    void fillFileInfo(FileInfo fileInfo, MultipartFile file);

    FileTree getFileTree(String directoryId, String filePath);

    PageData<ImageOption> getImageByPage(Page page);

    StatsResult fileDiskStats();

    List<StatsResult> fileDiskStatsByTenant();
}
