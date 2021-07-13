package com.fobgochod.service.client;

import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.domain.select.ImageOption;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.service.client.base.EntityService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoCrudService extends EntityService<FileInfo> {

    List<FileInfo> findByDirId(String directoryId);

    FileInfo findByDirIdAndName(String directoryId, String fileName);

    FileInfo getFileInfo(FileInfo fileInfo);

    FileInfo getFileInfo(FileInfo fileInfo, MultipartFile file);

    FileTree getFileTree(String directoryId, String filePath);

    PageData<ImageOption> getImageByPage(Page page);
}
