package com.fobgochod.service.business.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.DirTree;
import com.fobgochod.domain.enumeration.FileTypeEnum;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.FileOpTree;
import com.fobgochod.domain.FileOpTreeContextHolder;
import com.fobgochod.entity.File;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.business.FileService;
import com.fobgochod.service.client.DirectoryCrudService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.RecycleCrudService;
import com.fobgochod.service.client.ShareCrudService;
import com.fobgochod.service.client.ShrinkCrudService;
import com.fobgochod.service.mongo.FilesCollection;
import com.fobgochod.service.mongo.FileStorage;
import com.fobgochod.util.FileUtil;
import com.fobgochod.util.IdUtil;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FilesCollection filesCollection;
    @Autowired
    private ShareCrudService shareCrudService;
    @Autowired
    private ShrinkCrudService shrinkCrudService;
    @Autowired
    private RecycleCrudService recycleCrudService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @Override
    public String uploadFileStream(FileInfo fileInfo, InputStream source) {
        return uploadFile(fileInfo, source).getId();
    }

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, InputStream source) {
        String fileInfoId = null;
        try {
            fileInfo = fileInfoCrudService.getFileInfo(fileInfo);
            fileInfoId = fileInfoCrudService.insert(fileInfo);

            String fileId = fileStorage.uploadFromStream(fileInfo.getName(), source);
            long size = fileStorage.getFileLength(fileId);
            fileInfo.setFileId(new ObjectId(fileId));
            fileInfo.setSize(size);
            fileInfo.setCompleted(Boolean.TRUE);
            fileInfoCrudService.update(fileInfo);
            this.incrReferenceCount(new ObjectId(fileId));

            return fileInfo;
        } catch (Exception e) {
            this.deleteFile(fileInfoId, true);
            throw new SystemException("File upload failed !");
        }
    }

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, InputStream source, Authentication authentication) {
        UserUtil.setAuthentication(authentication);
        return uploadFile(fileInfo, source);
    }

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, MultipartFile file) {
        String fileInfoId = null;
        try {
            fileInfo = fileInfoCrudService.getFileInfo(fileInfo, file);
            fileInfoId = fileInfoCrudService.insert(fileInfo);

            String fileId = fileStorage.uploadFromStream(fileInfo.getName(), file.getInputStream());
            long size = fileStorage.getFileLength(fileId);
            fileInfo.setFileId(new ObjectId(fileId));
            fileInfo.setSize(size);
            fileInfo.setCompleted(Boolean.TRUE);
            fileInfoCrudService.update(fileInfo);
            this.incrReferenceCount(IdUtil.getObjectId(fileId));
            return fileInfo;
        } catch (Exception e) {
            this.deleteFile(fileInfoId, true);
            throw new SystemException(String.format("上传失败 %s", e.getMessage()));
        }
    }

    @Override
    public FileInfo uploadFile(FileInfo fileInfo, MultipartFile file, Authentication authentication) {
        UserUtil.setAuthentication(authentication);
        return uploadFile(fileInfo, file);
    }

    @Override
    public void incrReferenceCount(ObjectId fileId) {
        if (fileId != null) {
            filesCollection.referenceCount(fileId, 1);
        }
    }

    @Override
    public void decrReferenceCount(ObjectId fileId) {
        if (fileId != null) {
            filesCollection.referenceCount(fileId, -1);
            //检查引用计数，若小于等于0，则删除文件
            int count = fileStorage.getFileMetadataCount(fileId);
            if (count <= 0) {
                fileStorage.deleteFile(fileId);
            }
        }
    }

    @Override
    public RecycleBin deleteFile(String fileInfoId) {
        return deleteFile(fileInfoId, false);
    }

    @Override
    public RecycleBin deleteFile(String fileInfoId, boolean force) {
        FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
        if (fileInfo == null) {
            return null;
        }
        deleteFile0(fileInfoId, force);
        if (!force) {
            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setName(fileInfo.getName());
            recycleBin.setDeleteDate(LocalDateTime.now());
            recycleBin.setSize(fileInfo.getSize());
            recycleBin.setContentType(FileTypeEnum.File.toString());
            UserUtil.copyCreate(recycleBin, fileInfo);

            List<DirInfo> dirInfos = directoryCrudService.getDirInfos(fileInfo.getDirectoryId());
            recycleBin.setFileInfo(fileInfo);
            recycleBin.setDirInfos(dirInfos);
            recycleBin.setPaths(FileUtil.getPaths(fileInfo.getDirectoryId(), dirInfos));
            recycleCrudService.save(recycleBin);
            return recycleBin;
        }
        return null;
    }

    private void deleteFile0(String fileInfoId, boolean force) {
        if (force) {
            FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
            if (fileInfo != null) {
                decrReferenceCount(fileInfo.getFileId());
            }
        }
        fileInfoCrudService.deleteById(fileInfoId);
        shareCrudService.deleteByFileId(fileInfoId);
        shrinkCrudService.deleteByTargetId(fileInfoId);
    }

    @Override
    public RecycleBin deleteDir(String dirId) {
        return deleteDir(dirId, false);
    }

    @Override
    public RecycleBin deleteDir(String dirId, boolean force) {
        DirInfo dirInfo = directoryCrudService.findById(dirId);
        if (dirInfo == null) {
            return null;
        }
        DirTree dirTree = this.deleteDir0(dirInfo, force);
        if (!force) {
            RecycleBin recycleBin = new RecycleBin();
            recycleBin.setName(dirInfo.getName());
            recycleBin.setDeleteDate(LocalDateTime.now());
            recycleBin.setContentType(FileTypeEnum.Directory.toString());
            UserUtil.copyCreate(recycleBin, dirInfo);

            List<DirInfo> dirInfos = directoryCrudService.getDirInfos(dirInfo.getParentId());
            recycleBin.setDirInfos(dirInfos);
            recycleBin.setPaths(FileUtil.getPaths(dirInfo.getParentId(), dirInfos));
            recycleBin.setSize(FileUtil.getDirSize(dirTree));
            recycleBin.setDirInfo(dirTree);
            recycleCrudService.save(recycleBin);
            return recycleBin;
        }
        return null;
    }

    private DirTree deleteDir0(DirInfo dirInfo, boolean force) {
        DirTree dirTree = new DirTree(dirInfo);
        //删除文件
        List<FileInfo> fileInfos = fileInfoCrudService.findByDirId(dirInfo.getId());
        dirTree.setFiles(fileInfos);
        for (FileInfo fileInfo : fileInfos) {
            deleteFile0(fileInfo.getId(), force);
        }
        // 删除目录
        List<DirInfo> dirInfos = directoryCrudService.findByParentId(dirInfo.getId());
        List<DirTree> dirs = new ArrayList<>();
        for (DirInfo childDirInfo : dirInfos) {
            dirs.add(deleteDir0(childDirInfo, force));
        }
        dirTree.setDirs(dirs);
        directoryCrudService.deleteById(dirInfo.getId());
        return dirTree;
    }

    @Override
    public void deleteRecycleBin(String recycleBinId) {
        RecycleBin recycleBin = recycleCrudService.findById(recycleBinId);
        if (recycleBin != null) {
            if (FileTypeEnum.File.toString().equals(recycleBin.getContentType())) {
                FileInfo fileInfo = recycleBin.getFileInfo();
                decrReferenceCount(fileInfo.getFileId());
            }
            if (FileTypeEnum.Directory.toString().equals(recycleBin.getContentType())) {
                DirTree dirTree = recycleBin.getDirInfo();
                decrReferenceCount0(dirTree);
            }
            recycleCrudService.deleteById(recycleBinId);
        }
    }

    private void decrReferenceCount0(DirTree dirTree) {
        for (FileInfo file : dirTree.getFiles()) {
            decrReferenceCount(file.getFileId());
        }
        for (DirTree dir : dirTree.getDirs()) {
            decrReferenceCount0(dir);
        }
    }

    @Override
    public void restoreFile(String recycleBinId) {
        RecycleBin recycleBin = recycleCrudService.findById(recycleBinId);
        if (recycleBin != null) {
            if (FileTypeEnum.File.toString().equals(recycleBin.getContentType())) {
                FileInfo fileInfo = recycleBin.getFileInfo();
                this.recoverDirectory(fileInfo.getDirectoryId(), recycleBin.getDirInfos());
                fileInfoCrudService.insert(fileInfo);
            }
            if (FileTypeEnum.Directory.toString().equals(recycleBin.getContentType())) {
                DirTree dirTree = recycleBin.getDirInfo();
                this.recoverDirectory(dirTree.getParentId(), recycleBin.getDirInfos());
                //还原最外层目录
                DirInfo dirInfo = directoryCrudService.findById(dirTree.getId());
                this.createDirectory(dirInfo, dirTree);
                this.recoverRecycleBin(dirTree);
            }
            recycleCrudService.deleteById(recycleBinId);
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
        return fileOpTree.getTargetId().toString();
    }

    private FileOpTree copyFile0(FileInfo fileInfo, String directoryId) {
        try {
            if (fileUnCompleted(fileInfo)) {
                throw new SystemException("文件正在上传中，不能复制！");
            }
            //判断文件夹名称是否已存在
            FileInfo newFileInfo = fileInfo.clone();
            newFileInfo.setId(null);
            newFileInfo.setName(this.getDupFileName0(directoryId, fileInfo.getName(), false));
            newFileInfo.setDirectoryId(IdUtil.getDirId(directoryId));
            String newFileInfoId = fileInfoCrudService.insert(newFileInfo);
            incrReferenceCount(fileInfo.getFileId());
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
            throw new SystemException(String.format("目录已经不存在，请刷新重试！[%s]", dirId));
        } else {
            // 操作记录
            directoryCrudService.update(dirInfo);
        }
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
        if (fileInfo == null) {
            throw new SystemException(String.format("文件不存在，请刷新重试！[%s]", fileInfoId));
        }
        if (fileUnCompleted(fileInfo)) {
            throw new SystemException(String.format("文件正在上传中，不能操作！[%s]", fileInfoId));
        }
        // 操作记录
        fileInfoCrudService.update(fileInfo);
        return fileInfo;
    }

    private FileInfo fileOpCheck(String fileInfoId, String targetDirId) {
        if (!directoryCrudService.exists(targetDirId)) {
            throw new SystemException("目标目录不存在，复制失败！");
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
    public boolean fileUnCompleted(FileInfo fileInfo) {
        if (fileInfo != null && fileInfo.isCompleted()) {
            File file = fileStorage.getFile(fileInfo.getFileId().toString());
            return file == null;
        }
        return true;
    }
}
