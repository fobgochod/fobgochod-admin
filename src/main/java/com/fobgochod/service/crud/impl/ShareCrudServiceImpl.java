package com.fobgochod.service.crud.impl;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.EnvProperties;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.domain.enumeration.ShareType;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.exception.SystemException;
import com.fobgochod.service.crud.FileInfoCrudService;
import com.fobgochod.service.crud.ShareCrudService;
import com.fobgochod.service.crud.base.BaseEntityService;
import com.fobgochod.service.file.FileOpService;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ShareCrudServiceImpl extends BaseEntityService<ShareRecord> implements ShareCrudService {

    @Autowired
    private FileOpService fileOpService;
    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;


    @Override
    public String shareFile(FileInfo fileInfo) {
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setId(SnowFlake.getInstance().get());
        shareRecord.setUserId(UserUtil.getUserId());
        shareRecord.setUserName(UserUtil.getUserName());
        shareRecord.setFileId(fileInfo.getId());
        shareRecord.setFileName(fileInfo.getName());
        shareRecord.setUrl(getShareUrl(shareRecord.getId()));
        shareRecord.setFileIds(Collections.singletonList(shareRecord.getFileId()));
        this.insert(shareRecord);
        // 操作记录
        fileInfoCrudService.update(fileInfo);
        return shareRecord.getUrl();
    }


    @Override
    public void deleteByFileId(String fileInfoId) {
        MongoCollection<ShareRecord> mongoCollection = this.getCollection();
        mongoCollection.deleteMany(Filters.eq("fileId", fileInfoId));
    }

    @Override
    public ShareRecord shareFile(BatchFid batchFid) {
        this.fileCheck(batchFid);
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setId(SnowFlake.getInstance().get());
        shareRecord.setUserId(UserUtil.getUserId());
        shareRecord.setUserName(UserUtil.getUserName());
        if (batchFid.getFileIds().size() == 1 && batchFid.getDirIds().isEmpty()) {
            String fileInfoId = batchFid.getFileIds().get(0);
            FileInfo fileInfo = fileInfoCrudService.findById(fileInfoId);
            if (fileInfo == null) {
                throw new SystemException("文件不存在" + batchFid.getFileIds());
            }
            shareRecord.setType(ShareType.One);
            shareRecord.setFileId(fileInfo.getId());
            shareRecord.setFileName(fileInfo.getName());
        } else {
            shareRecord.setType(ShareType.Mix);
        }
        shareRecord.setUrl(this.getShareUrl(shareRecord.getId()));
        shareRecord.setExpireDate(batchFid.getExpireDate());
        shareRecord.setFileIds(batchFid.getFileIds());
        shareRecord.setDirIds(batchFid.getDirIds());
        this.insert(shareRecord);
        return shareRecord;
    }

    private void fileCheck(BatchFid batchFid) {
        for (String fileId : batchFid.getFileIds()) {
            fileOpService.fileOpCheck(fileId);
        }
        for (String dirId : batchFid.getDirIds()) {
            fileOpService.dirOpCheck(dirId);
        }
    }

    @Override
    public ShareRecord getShareUnExpired(String shareId) {
        ShareRecord shareRecord = this.findById(shareId);
        if (shareRecord == null || shareRecord.getExpireDate() == null) {
            throw new SystemException(I18nCode.FILE_SHARED_NONE, shareId);
        }
        if (LocalDateTime.now().isAfter(shareRecord.getExpireDate())) {
            throw new SystemException(I18nCode.FILE_SHARED_EXPIRE, shareRecord.getId(), shareRecord.getExpireDate());
        }
        return shareRecord;
    }

    @Override
    public List<ShareRecord> getShareFileByFileId(String fileId) {
        Bson filter = Filters.in("fileId", fileId);
        MongoCollection<ShareRecord> mongoCollection = this.getCollection();
        MongoCursor<ShareRecord> cursor = mongoCollection.find(filter).iterator();
        List<ShareRecord> lists = new ArrayList<>();
        while (cursor.hasNext()) {
            lists.add(cursor.next());
        }
        return lists;
    }

    private String getShareUrl(String shareId) {
        return String.format("%s/file/share/%s", envProperties.getBaseUri(), shareId);
    }
}
