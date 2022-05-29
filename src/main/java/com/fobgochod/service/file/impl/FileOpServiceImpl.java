package com.fobgochod.service.file.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.DirTree;
import com.fobgochod.domain.FileOpTree;
import com.fobgochod.domain.FileOpTreeContextHolder;
import com.fobgochod.domain.base.BatchFid;
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
import com.fobgochod.util.IdUtil;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import org.springframework.beans.BeanUtils;
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
            shrinkCrudService.deleteByTargetId(fileInfoId);
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

    private void createDirectory(DirInfo dirInfo, DirTree dirTree) {
        if (dirInfo == null) {
            dirInfo = new DirInfo();
            BeanUtils.copyProperties(dirTree, dirInfo);
            directoryCrudService.insert(dirInfo);
        }
    }

    private void recoverRecycleBin(DirTree dirTree) {
        for (FileInfo fileInfo : dirTree.getFiles()) {
            fileInfoCrudService.insert(fileInfo);
        }
        for (DirTree tree : dirTree.getDirs()) {
            DirInfo dirInfo = directoryCrudService.findById(tree.getId());
            this.createDirectory(dirInfo, tree);
            recoverRecycleBin(tree);
        }
    }

    private void recoverDirectory(String dirId, List<DirInfo> dirInfos) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        //目录不存在新建目录
        if (dirInfo == null) {
            for (DirInfo dir : dirInfos) {
                if (!directoryCrudService.exists(dir.getId())) {
                    directoryCrudService.insert(dir);
                }
            }
        }
    }

    @Override
    public void moveFile(String fileInfoId, String directoryId) {
        FileInfo fileInfo = fileOpCheck(fileInfoId, directoryId);
        if (fileInfo.getDirectoryId().equals(directoryId)) {
            // 就在目标目录下面，不用动
            return;
        }
        fileInfo.setName(this.getDupFileName(directoryId, fileInfo.getName()));
        fileInfo.setDirectoryId(IdUtil.getDirId(directoryId));
        fileInfoCrudService.update(fileInfo);
    }

    @Override
    public void moveDir(String dirId, String targetDirId) {
        DirInfo dirInfo = dirOpCheck(dirId, targetDirId);
        if (dirInfo.getParentId().equals(targetDirId)) {
            // 就在目标目录下面，不用动
            return;
        }
        dirInfo.setName(this.getDupDirName(targetDirId, dirInfo.getName(), false));
        dirInfo.setParentId(targetDirId);
        directoryCrudService.update(dirInfo);
    }

    @Override
    public String copyFile(String fileInfoId, String directoryId) {
        FileInfo fileInfo = this.fileOpCheck(fileInfoId, directoryId);
        FileOpTree fileOpTree = this.copyFile0(fileInfo, IdUtil.getDirId(directoryId));
        FileOpTreeContextHolder.getContext().add(fileOpTree);
        return fileOpTree.getTargetId();
    }

    private FileOpTree copyFile0(FileInfo fileInfo, String directoryId) {
        try {
            this.checkFileBeforeUse(fileInfo);
            //判断文件夹名称是否已存在
            FileInfo newFileInfo = fileInfo.clone();
            newFileInfo.setId(null);
            newFileInfo.setName(this.getDupFileName0(directoryId, fileInfo.getName(), false));
            newFileInfo.setDirectoryId(IdUtil.getDirId(directoryId));
            String newFileInfoId = fileInfoCrudService.insert(newFileInfo);
            // 记录复制结果
            return FileOpTree.fileOpOk(fileInfo.getId(), newFileInfoId);
        } catch (Exception e) {
            // 记录复制结果
            return FileOpTree.fileOpFail(fileInfo.getId(), e.getMessage());
        }
    }

    @Override
    public String copyDir(String dirId, String targetDirId) {
        DirInfo dirInfo = this.dirOpCheck(dirId, targetDirId);
        DirInfo newDirInfo = dirInfo.clone();
        newDirInfo.setId(null);
        newDirInfo.setName(this.getDupDirName(targetDirId, dirInfo.getName(), false));
        newDirInfo.setParentId(targetDirId);
        String newDirId = directoryCrudService.insert(newDirInfo);
        // 记录返回结果
        FileOpTree dirOpTree = this.copyDir0(dirId, newDirId);
        FileOpTreeContextHolder.getContext().add(dirOpTree);
        return newDirId;
    }

    private FileOpTree copyDir0(String dirId, String newDirId) {
        // 记录复制结果
        FileOpTree fileOpTree = FileOpTree.dirOpOk(dirId, newDirId);
        List<FileInfo> fileInfos = fileInfoCrudService.findByDirId(dirId);
        for (FileInfo fileInfo : fileInfos) {
            // 记录复制结果
            FileOpTree fileOp = this.copyFile0(fileInfo, IdUtil.getDirId(newDirId));
            fileOpTree.getFileOps().add(fileOp);
        }
        List<DirInfo> dirInfos = directoryCrudService.findByParentId(dirId);
        for (DirInfo dirInfo : dirInfos) {
            DirInfo newDirInfo = dirInfo.clone();
            newDirInfo.setId(SnowFlake.getInstance().get());
            newDirInfo.setParentId(newDirId);
            String tempDirId = directoryCrudService.insert(newDirInfo);
            // 记录复制结果
            FileOpTree dirOp = this.copyDir0(dirInfo.getId(), tempDirId);
            fileOpTree.getFileOps().add(dirOp);
        }
        return fileOpTree;
    }

    private String getDupDirName(String targetDirId, String dirName, boolean flag) {
        DirInfo dirInfo = directoryCrudService.findByName(targetDirId, dirName);
        if (dirInfo != null) {
            String tempName = dirName + BaseField.DUPLICATE;
            if (flag) {
                tempName = FileUtil.getDupName(dirName);
            }
            DirInfo dupDirInfo = directoryCrudService.findByName(targetDirId, tempName);
            if (dupDirInfo == null) {
                return tempName;
            }
            return getDupDirName(targetDirId, tempName, true);
        }
        return dirName;
    }

    @Override
    public DirInfo dirOpCheck(String dirId) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        if (dirInfo == null) {
            throw new SystemException(I18nCode.FILE_DIR_NONE, dirId);
        }
        // 操作记录
        directoryCrudService.update(dirInfo);
        return dirInfo;
    }

    private DirInfo dirOpCheck(String dirId, String targetDirId) {
        if (IdUtil.isRootDir(dirId)) {
            throw new SystemException("根目录不允许操作");
        }
        if (dirId.equals(targetDirId)) {
            throw new SystemException("被操作的目录和目标目录不能相同");
        }
        if (!directoryCrudService.exists(targetDirId)) {
            throw new SystemException("目标目录已经不存在，请刷新重试");
        }
        return dirOpCheck(dirId);
    }

    private String getDupFileName(String targetDirId, String fileName) {
        FileInfo dupName = fileInfoCrudService.findByDirIdAndName(targetDirId, fileName);
        if (dupName != null) {
            return FileUtil.getDupFileName(fileName);
        }
        return fileName;
    }

    private String getDupFileName0(String targetDirId, String fileName, boolean flag) {
        FileInfo fileInfo = fileInfoCrudService.findByDirIdAndName(targetDirId, fileName);
        if (fileInfo != null) {
            String tempName = FileUtil.getDupFileName(fileName);
            if (flag) {
                tempName = FileUtil.getDupFileName0(fileName);
            }
            FileInfo dupFileInfo = fileInfoCrudService.findByDirIdAndName(targetDirId, fileName);
            if (dupFileInfo == null) {
                return tempName;
            }
            return getDupFileName0(targetDirId, tempName, true);
        }
        return fileName;
    }

    @Override
    public FileInfo fileOpCheck(String fileInfoId) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        this.checkFileBeforeUse(fileInfo);
        // 操作记录
        fileInfoCrudService.update(fileInfo);
        return fileInfo;
    }

    private FileInfo fileOpCheck(String fileInfoId, String targetDirId) {
        if (!directoryCrudService.exists(targetDirId)) {
            throw new SystemException(I18nCode.FILE_DIR_NONE);
        }
        return fileOpCheck(fileInfoId);
    }

    @Override
    public List<FileOpTree> batchCopy(BatchFid batchFid, String targetDirId) {
        for (String fileId : batchFid.getFileIds()) {
            try {
                this.copyFile(IdUtil.getDirId(fileId), IdUtil.getDirId(targetDirId));
            } catch (Exception e) {
                FileOpTree fileOpTree = FileOpTree.fileOpFail(IdUtil.getDirId(fileId), e.getMessage());
                FileOpTreeContextHolder.getContext().add(fileOpTree);
            }
        }
        for (String dirId : batchFid.getDirIds()) {
            try {
                this.copyDir(dirId, targetDirId);
            } catch (Exception e) {
                FileOpTree dirOpTree = FileOpTree.dirOpFail(dirId, e.getMessage());
                FileOpTreeContextHolder.getContext().add(dirOpTree);
            }
        }
        return FileOpTreeContextHolder.getContext();
    }

    @Override
    public void checkFileBeforeUse(FileInfo fileInfo) {
        if (fileInfo == null) {
            throw new SystemException(I18nCode.FILE_INFO_NONE);
        }
        if (!fileInfo.isCompleted()) {
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
