package com.fobgochod.service.client.impl;

import com.fobgochod.constant.BaseField;
import com.fobgochod.domain.EnvProperties;
import com.fobgochod.domain.enumeration.ShareType;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.serializer.Constants;
import com.fobgochod.service.business.FileService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.ShareCrudService;
import com.fobgochod.service.client.base.BaseEntityService;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ShareCrudServiceImpl extends BaseEntityService<ShareRecord> implements ShareCrudService {

    @Autowired
    private FileService fileService;
    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public void deleteByFileId(String fileInfoId) {
        MongoCollection<ShareRecord> mongoCollection = this.getCollection();
        mongoCollection.deleteMany(Filters.eq("fileId", fileInfoId));
    }

    @Override
    public long deleteByIdIn(Collection<String> ids) {
        MongoCollection<ShareRecord> mongoCollection = this.getCollection();
        DeleteResult deleteResult = mongoCollection.deleteMany(Filters.in(BaseField.ID, ids));
        return deleteResult.getDeletedCount();
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
                throw new BusinessException("文件不存在" + batchFid.getFileIds());
            }
            shareRecord.setType(ShareType.One);
            shareRecord.setFileId(fileInfo.getId().toString());
            shareRecord.setFileName(fileInfo.getName());
        } else {
            shareRecord.setType(ShareType.Mix);
        }
        shareRecord.setUrl(this.getShareUrl(shareRecord.getId()));
        if (batchFid.getExpireDate() == null) {
            shareRecord.setExpired(false);
        } else {
            shareRecord.setExpired(true);
            shareRecord.setExpireDate(batchFid.getExpireDate().format(Constants.DATETIME_FORMATTER));
        }
        shareRecord.setFileIds(batchFid.getFileIds());
        shareRecord.setDirIds(batchFid.getDirIds());
        this.insert(shareRecord);
        return shareRecord;
    }

    private void fileCheck(BatchFid batchFid) {
        for (String fileId : batchFid.getFileIds()) {
            fileService.fileOpCheck(fileId);
        }
        for (String dirId : batchFid.getDirIds()) {
            fileService.dirOpCheck(dirId);
        }
    }

    @Override
    public boolean isShareExpired(ShareRecord shareRecord) {
        if (shareRecord == null) {
            return true;
        }
        if (!shareRecord.isExpired()) {
            return false;
        }
        return LocalDateTime.now().isAfter(LocalDateTime.parse(shareRecord.getExpireDate(), Constants.DATETIME_FORMATTER));
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
        return String.format("%s/api/dmc/v2/file/share/%s", envProperties.getBaseUri(), shareId);
    }
}
