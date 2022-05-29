package com.fobgochod.service.crud.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.FileTree;
import com.fobgochod.domain.StatsResult;
import com.fobgochod.domain.base.EnvProperties;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.base.PageData;
import com.fobgochod.domain.enumeration.MimeType;
import com.fobgochod.domain.select.ImageOption;
import com.fobgochod.entity.file.DirInfo;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.crud.DirectoryCrudService;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.crud.base.BaseEntityService;
import com.fobgochod.service.mongo.FilesCollection;
import com.fobgochod.util.FileUtil;
import com.fobgochod.util.IdUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileInfoCrudServiceImpl extends BaseEntityService<FileInfo> implements FileInfoCrudService {

    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private FilesCollection filesCollection;
    @Autowired
    private DirectoryCrudService directoryCrudService;

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
        Bson filter = Filters.and(Filters.eq(BaseField.DIRECTORY_ID, directoryId), Filters.eq(BaseField.FILE_NAME, fileName));
        return mongoCollection.find(filter).first();
    }

    @Override
    public boolean needDelete(String id, ObjectId fileId) {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson filter = Filters.and(Filters.ne(BaseField.ID, id), Filters.eq(BaseField.FILE_ID, fileId));
        return mongoCollection.find(filter).first() == null;
    }

    @Override
    public void fillFileInfo(FileInfo fileInfo) {
        if (fileInfo == null || StringUtils.isEmpty(fileInfo.getName())) {
            throw new SystemException(I18nCode.FILE_NAME_NONE);
        }
        String dirId = IdUtil.getDirId(fileInfo.getDirectoryId());
        if (!directoryCrudService.exists(dirId)) {
            throw new SystemException(I18nCode.FILE_DIR_NONE);
        }
        fileInfo.setDirectoryId(dirId);

        String fileName = fileInfo.getName();
        if (fileInfo.getDisplayName() == null) {
            fileInfo.setDisplayName(FileUtil.getFileNameNoExt(fileName));
        }
        if (fileInfo.getExtension() == null) {
            fileInfo.setExtension(FileUtil.getFileExt(fileName));
        }
        fileInfo.setCompleted(false);
    }

    @Override
    public void fillFileInfo(FileInfo fileInfo, MultipartFile file) {
        if (fileInfo.getName() == null) {
            fileInfo.setName(file.getOriginalFilename());
        }
        if (fileInfo.getExtension() == null) {
            fileInfo.setExtension(FileUtil.getFileExt(fileInfo.getName()));
        }
        if (fileInfo.getContentType() == null) {
            fileInfo.setContentType(file.getContentType());
        }
        fileInfo.setSize(file.getSize());
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
    public PageData<ImageOption> getImageByPage(Page page) {
        if (page == null) {
            page = new Page();
        }
        Bson condFilter = page.filter();
        Bson imageFilter = Filters.or(Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_JPEG), Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_PNG), Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_BMP), Filters.eq(BaseField.CONTENT_TYPE, MimeType.IMAGE_GIF));
        Bson filter = Filters.and(imageFilter, condFilter);
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        long total = mongoCollection.countDocuments(filter);
        if (total <= 0) {
            return PageData.zero();
        }
        MongoCursor<FileInfo> iterator = mongoCollection.find(filter).sort(page.sort()).skip(page.skip()).limit(page.limit()).iterator();
        List<ImageOption> lists = new ArrayList<>();
        while (iterator.hasNext()) {
            FileInfo fileInfo = iterator.next();
            ImageOption option = new ImageOption();
            option.setFileId(fileInfo.getId());
            option.setFileName(fileInfo.getName());
            option.setUri(String.format("%s%s/file/preview/%s", envProperties.getBaseUri(), envProperties.getContextPath(), fileInfo.getId()));
            lists.add(option);
        }
        return PageData.data(total, lists);
    }

    /**
     * @formatter:off
     *  db.getCollection("fileinfo").aggregate([
     *     {
     *         $group: {
     *             _id: null,
     *             size: { $sum: "$size" },
     *             count: { $sum: 1 }
     *         }
     *     },
     * ])
     * @formatter:on
     */
    @Override
    public StatsResult fileDiskStats() {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson group = Aggregates.group(null, Accumulators.sum("size", "$size"), Accumulators.sum("count", 1));
        return mongoCollection.aggregate(Arrays.asList(group), StatsResult.class).first();
    }

    /**
     * @formatter:off
     * db.getCollection("fileinfo").aggregate([
     *     {
     *         $group: {
     *             _id: "$tenantId",
     *             size: { $sum: "$size" },
     *             count: { $sum: 1 }
     *         }
     *     },
     * ])
     * @formatter:on
     */
    @Override
    public List<StatsResult> fileDiskStatsByTenant() {
        MongoCollection<FileInfo> mongoCollection = this.getCollection();
        Bson group = Aggregates.group("$tenantId", Accumulators.sum("size", "$size"), Accumulators.sum("count", 1));
        Bson projection = Aggregates.project(new Document("tenantId", "$_id").append("size", "$size").append("count", "$count").append("_id", false));
        return mongoCollection.aggregate(Arrays.asList(group, projection), StatsResult.class).into(new ArrayList<>());
    }

    @Override
    public void dropCollection() {
        super.dropCollection();
        filesCollection.drop();
    }
}
