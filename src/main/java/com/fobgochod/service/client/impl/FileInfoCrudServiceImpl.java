package com.fobgochod.service.client.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.constant.I18nError;
import com.fobgochod.domain.EnvProperties;
import com.fobgochod.domain.RecentFile;
import com.fobgochod.domain.enumeration.MimeType;
import com.fobgochod.domain.select.ImageOption;
import com.fobgochod.domain.v2.FileTree;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.domain.v2.PageData;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.service.business.FileService;
import com.fobgochod.service.client.DirectoryCrudService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.base.BaseEntityService;
import com.fobgochod.service.mongo.FileStorage;
import com.fobgochod.service.mongo.FilesCollection;
import com.fobgochod.util.FileUtil;
import com.fobgochod.util.IdUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileInfoCrudServiceImpl extends BaseEntityService<FileInfo> implements FileInfoCrudService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileService fileService;
    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    @Override
    public List<FileInfo> findLtModifyDate(LocalDateTime modifyDate) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        MongoCursor<FileInfo> iterator = mongoCollection.find(Filters.lt("modifyDate", modifyDate)).iterator();
        List<FileInfo> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public List<FileInfo> findGtModifyDate(LocalDateTime modifyDate) {
        if (modifyDate == null) {
            modifyDate = LocalDateTime.now().minusMonths(1L);
        }
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        MongoCursor<FileInfo> iterator = mongoCollection.find(Filters.gt("modifyDate", modifyDate)).iterator();
        List<FileInfo> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public List<FileInfo> getFileInfoUncompleted() {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson sort = Sorts.descending("modifyDate");
        final Bson query = Filters.eq(BaseField.COMPLETED, false);
        FindIterable<FileInfo> lists = mongoCollection.find(query).sort(sort);
        List<FileInfo> fileInfos = new ArrayList<>();
        for (FileInfo list : lists) {
            fileInfos.add(list);
        }
        return fileInfos;
    }

    @Override
    public List<RecentFile> getFileInfoRecently() {
        List<FileInfo> fileInfos = this.findGtModifyDate(null);
        List<RecentFile> recentFiles = new ArrayList<>();
        for (FileInfo fileInfo : fileInfos) {
            RecentFile recentFile = new RecentFile();
            try {
                BeanUtils.copyProperties(fileInfo, recentFile);
                if (BaseField.EMPTY_UUID_STR.equals(fileInfo.getDirectoryId())) {
                    recentFile.setDirectoryName("根目录");
                } else {
                    DirInfo dirInfo = directoryCrudService.findById(fileInfo.getDirectoryId());
                    recentFile.setDirectoryName(dirInfo.getName());
                }
            } catch (Exception e) {
                throw new BusinessException(e);
            }
            recentFiles.add(recentFile);
        }
        return recentFiles;
    }

    @Override
    public List<FileInfo> findExpired() {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        MongoCursor<FileInfo> iterator = mongoCollection.find(Filters.lt("expireDate", LocalDateTime.now())).iterator();
        List<FileInfo> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            lists.add(iterator.next());
        }
        return lists;
    }

    @Override
    public List<FileInfo> findByDirId(String directoryId) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        FindIterable<FileInfo> iterable = mongoCollection.find(Filters.eq(BaseField.DIRECTORY_ID, directoryId));
        List<FileInfo> lists = new ArrayList<>();
        for (FileInfo fileInfo : iterable) {
            lists.add(fileInfo);
        }
        return lists;
    }

    @Override
    public FileInfo findByDirIdAndName(String directoryId, String fileName) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson filter = Filters.and(
                Filters.eq(BaseField.DIRECTORY_ID, directoryId),
                Filters.eq(BaseField.FILE_NAME, fileName)
        );
        return mongoCollection.find(filter).first();
    }

    @Override
    public List<FileInfo> findByDirId(String dirId, Bson sort) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson query = Filters.eq(BaseField.DIRECTORY_ID, dirId);
        MongoCursor<FileInfo> result = mongoCollection.find(query).sort(sort).iterator();
        List<FileInfo> lists = new ArrayList<>();
        while (result.hasNext()) {
            lists.add(result.next());
        }
        return lists;
    }

    @Override
    public long forceDeleteFile(LocalDateTime modifyDate) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        List<FileInfo> fileInfos = this.findLtModifyDate(modifyDate);
        if (fileInfos.isEmpty()) {
            return 0;
        }
        fileInfos.forEach(fileInfo -> {
            fileStorage.deleteFile(fileInfo.getFileId());
        });
        return mongoCollection.deleteMany(Filters.lt("modifyDate", modifyDate)).getDeletedCount();
    }

    @Override
    public FileInfo getFileInfo(FileInfo fileInfo) {
        if (fileInfo == null || StringUtils.isEmpty(fileInfo.getName())) {
            throw new BusinessException(I18nError.ERROR_10003);
        }
        String dirId = IdUtil.getDirectoryId(fileInfo.getDirectoryId());
        if (!directoryCrudService.exists(dirId)) {
            throw new BusinessException("上传目录不存在，请重新选择");
        }
        fileInfo.setDirectoryId(dirId);

        String fileName = fileInfo.getName();
        if (fileInfo.getDisplayName() == null) {
            fileInfo.setDisplayName(FileUtil.getFileNameNoExt(fileName));
        }
        if (fileInfo.getExtension() == null) {
            fileInfo.setExtension(FileUtil.getFileExt(fileName));
        }
        if (fileInfo.getContentType() == null) {
        }
        fileInfo.setCompleted(false);
        return fileInfo;
    }

    @Override
    public FileInfo getFileInfo(FileInfo fileInfo, MultipartFile file) {
        if (file != null) {
            fileInfo = fileInfo == null ? new FileInfo() : fileInfo;
            if (fileInfo.getName() == null) {
                fileInfo.setName(file.getOriginalFilename());
            }
            fileInfo.setSize(file.getSize());
            if (fileInfo.getExtension() == null) {
                fileInfo.setExtension(FileUtil.getFileExt(fileInfo.getName()));
            }
            if (fileInfo.getContentType() == null) {
                fileInfo.setContentType(file.getContentType());
            }
        }
        return getFileInfo(fileInfo);
    }

    @Override
    public FileTree getFileTree(String directoryId, String filePath) {
        DirInfo dir = directoryCrudService.findById(directoryId);
        if (dir != null) {
            FileTree dirTree = new FileTree(dir.getId(), dir.getName(), true);
            if (filePath != null) {
                dirTree.setFilePath(filePath + java.io.File.separator + dir.getName());
            } else {
                dirTree.setFilePath(dir.getName());
            }
            // 处理目录下的文件
            List<FileTree> fileTrees = new ArrayList<>();
            List<FileInfo> fileInfos = this.findByDirId(directoryId);
            for (FileInfo fileInfo : fileInfos) {
                FileTree fileTree = new FileTree(fileInfo.getId(), fileInfo.getName());
                fileTree.setFilePath(dirTree.getFilePath() + java.io.File.separator + fileInfo.getName());
                fileTrees.add(fileTree);
            }
            dirTree.setFiles(fileTrees);
            // 处理子目录
            List<DirInfo> dirInfos = directoryCrudService.findByParentId(dir.getId());
            for (DirInfo dirInfo : dirInfos) {
                fileTrees.add(getFileTree(dirInfo.getId().toString(), dirTree.getFilePath()));
            }
            return dirTree;
        }
        return null;
    }

    @Override
    public List<FileInfo> getFileInfo(Bson filter, Bson fields, Bson sort) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        FindIterable<FileInfo> findIterable = mongoCollection.find(filter).projection(fields).sort(sort);
        List<FileInfo> fileInfos = new ArrayList<>();
        for (FileInfo document : findIterable) {
            fileInfos.add(document);
        }
        return fileInfos;
    }

    @Override
    public List<FileInfo> getFileInfo(Bson filter, Bson fields, Bson sort, int pageNum, int pageSize) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        FindIterable<FileInfo> findIterable = mongoCollection.find(filter).projection(fields).sort(sort).skip((pageNum - 1) * pageSize).limit(pageSize + 1);
        List<FileInfo> fileInfos = new ArrayList<>();
        for (FileInfo document : findIterable) {
            fileInfos.add(document);
        }
        return fileInfos;
    }

    @Override
    public FileInfo getByFileName(String directoryId, String fileName) {
        Bson filter = Filters.and(
                Filters.eq(BaseField.DIRECTORY_ID, directoryId),
                Filters.eq(BaseField.FILE_NAME, fileName)
        );
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        return mongoCollection.find(filter).first();
    }

    @Override
    public void changeFileName(String fileInfoId, String newFileName) {
        FileInfo fileInfo = this.findById(fileInfoId);
        if (fileService.fileUnCompleted(fileInfo)) {
            throw new BusinessException("源文件不存在或者正在上传中，不能改名！");
        }
        if (this.getByFileName(fileInfo.getDirectoryId(), newFileName) != null) {
            throw new BusinessException("目录下已经存在相同名称的文件，不能修改名称");
        }
        fileInfo.setName(newFileName);
        super.update(fileInfo);
    }

    @Override
    public PageData<ImageOption> getImageByPage(Page page) {
        if (page == null) {
            page = new Page();
        }
        Bson condFilter = page.filter();
        Bson imageFilter = Filters.or(
                Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_JPEG),
                Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_PNG),
                Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_BMP),
                Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_GIF)
        );
        Bson filter = Filters.and(imageFilter, condFilter);
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        long total = mongoCollection.countDocuments(filter);
        if (total <= 0) {
            return PageData.zero();
        }
        MongoCursor<FileInfo> iterator = mongoCollection.find(filter).sort(page.sort())
                .skip(page.skip()).limit(page.limit()).iterator();
        List<ImageOption> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            FileInfo fileInfo = iterator.next();
            ImageOption option = new ImageOption();
            option.setFileId(fileInfo.getId());
            option.setFileName(fileInfo.getName());
            option.setUri(String.format("%s/api/dmc/v2/file/preview/%s", envProperties.getBaseUri(), fileInfo.getId()));
            lists.add(option);
        }
        return PageData.data(total, lists);
    }

    @Autowired
    private FilesCollection filesCollection;

    @Override
    public void dropCollection() {
        super.dropCollection();
        filesCollection.drop();
    }
}
