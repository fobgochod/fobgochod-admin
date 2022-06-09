package com.fobgochod.service.file.impl;

import com.fobgochod.domain.DirTree;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.enumeration.FileTypeEnum;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.crud.DirectoryCrudService;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.crud.RecycleCrudService;
import com.fobgochod.service.crud.ShrinkCrudService;
import com.fobgochod.service.file.FileOpService;
import com.fobgochod.service.file.UploadService;
import com.fobgochod.util.FileUtil;
import com.fobgochod.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileOpServiceImpl implements FileOpService {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private ShrinkCrudService shrinkCrudService;
    @Autowired
    private RecycleCrudService recycleCrudService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @Override
    public RecycleBin deleteFile(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileInfo == null) {
            return null;
        }
        deleteFile0(fileInfo, false);
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setFileId(fileInfo.getId());
        recycleBin.setFileName(fileInfo.getName());
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBin.setSize(fileInfo.getSize());
        recycleBin.setType(FileTypeEnum.File);
        UserUtil.copyCreate(recycleBin, fileInfo);

        List<DirInfo> dirInfos = directoryCrudService.getDirInfos(fileInfo.getDirectoryId());
        recycleBin.setPaths(FileUtil.getPaths(fileInfo.getDirectoryId(), dirInfos));
        recycleCrudService.save(recycleBin);
        return recycleBin;
    }

    @Override
    public void deleteFileForce(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        deleteFile0(fileInfo, true);
    }

    @Override
    public RecycleBin deleteDir(String dirId) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        if (dirInfo == null) {
            return null;
        }
        DirTree dirTree = this.deleteDir0(dirInfo, false);
        RecycleBin recycleBin = new RecycleBin();
        recycleBin.setFileId(dirInfo.getId());
        recycleBin.setFileName(dirInfo.getName());
        recycleBin.setDeleteDate(LocalDateTime.now());
        recycleBin.setType(FileTypeEnum.Directory);
        UserUtil.copyCreate(recycleBin, dirInfo);

        List<DirInfo> dirInfos = directoryCrudService.getDirInfos(dirInfo.getParentId());
        recycleBin.setPaths(FileUtil.getPaths(dirInfo.getParentId(), dirInfos));
        recycleBin.setSize(FileUtil.getDirSize(dirTree));
        recycleCrudService.save(recycleBin);
        return recycleBin;
    }

    @Override
    public void deleteDirForce(String dirId) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        if (dirInfo == null) {
            return;
        }
        this.deleteDir0(dirInfo, true);
    }

    private DirTree deleteDir0(DirInfo dirInfo, boolean force) {
        DirTree dirTree = new DirTree(dirInfo);
        //删除文件
        List<FileInfo> fileInfos = fileInfoCrudService.findByDirId(dirInfo.getId());
        dirTree.setFiles(fileInfos);
        for (FileInfo fileInfo : fileInfos) {
            deleteFile0(fileInfo, force);
        }
        // 删除目录
        List<DirInfo> dirInfos = directoryCrudService.findByParentId(dirInfo.getId());
        List<DirTree> dirs = new ArrayList<>();
        for (DirInfo childDirInfo : dirInfos) {
            dirs.add(deleteDir0(childDirInfo, force));
        }
        dirTree.setDirs(dirs);
        if (force) {
            directoryCrudService.deleteById(dirInfo.getId());
        } else {
            directoryCrudService.removeById(dirInfo.getId());
        }
        return dirTree;
    }

    private void deleteFile0(FileInfo fileInfo, boolean force) {
        if (force) {
            uploadService.deleteFile(fileInfo);
        }
        if (fileInfo != null) {
            deleteFileInfo(fileInfo.getId(), force);
        }
    }

    private void deleteFileInfo(String fileInfoId, boolean force) {
        if (force) {
            fileInfoCrudService.deleteById(fileInfoId);
            shrinkCrudService.deleteByShrinkId(fileInfoId);
        } else {
            fileInfoCrudService.removeById(fileInfoId);
        }
    }

    @Override
    public void deleteRecycleBin(String recycleBinId) {
        RecycleBin recycleBin = recycleCrudService.findById(recycleBinId);
        if (recycleBin != null) {
            if (FileTypeEnum.File.equals(recycleBin.getType())) {
                fileInfoCrudService.deleteById(recycleBin.getFileId());
                this.deleteFileForce(recycleBin.getFileId());
            }
            if (FileTypeEnum.Directory.equals(recycleBin.getType())) {
                directoryCrudService.deleteById(recycleBin.getFileId());
                this.deleteDirForce(recycleBin.getFileId());
            }
            recycleCrudService.deleteById(recycleBinId);
        }
    }

    @Override
    public void restoreFile(String recycleBinId) {
        RecycleBin recycleBin = recycleCrudService.findById(recycleBinId);
        if (recycleBin != null) {
            if (FileTypeEnum.File.equals(recycleBin.getType())) {
                fileInfoCrudService.restoreById(recycleBin.getFileId());
            }
            if (FileTypeEnum.Directory.equals(recycleBin.getType())) {
                this.restoreDirInfo(recycleBin.getFileId());
            }
            recycleCrudService.deleteById(recycleBinId);
        }
    }

    private void restoreDirInfo(String dirId) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        if (dirInfo != null) {
            if (dirInfo.getParentId() != null) {
                restoreDirInfo(dirInfo.getParentId());
            }
            directoryCrudService.restoreById(dirInfo.getId());
            List<FileInfo> fileInfos = fileInfoCrudService.findByDirId(dirInfo.getId());
            for (FileInfo fileInfo : fileInfos) {
                fileInfoCrudService.restoreById(fileInfo.getId());
            }
        }
    }

    @Override
    public void clearRecycleBin() {
        List<RecycleBin> recycleBins = recycleCrudService.findAll();
        for (RecycleBin recyclebin : recycleBins) {
            this.deleteRecycleBin(recyclebin.getId());
        }
    }

    @Override
    public void checkFileBeforeUse(FileInfo fileInfo) {
        if (fileInfo == null) {
            throw new SystemException(I18nCode.FILE_INFO_NONE);
        }
        if (!fileInfo.getCompleted()) {
            throw new SystemException(I18nCode.FILE_UNCOMPLETED);
        }
    }

    @NonNull
    @Override
    public FileInfo findFileInfo(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        this.checkFileBeforeUse(fileInfo);
        return fileInfo;
    }
}
