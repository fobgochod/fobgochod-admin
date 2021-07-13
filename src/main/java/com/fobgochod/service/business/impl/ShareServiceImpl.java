package com.fobgochod.service.business.impl;

import com.fobgochod.domain.base.EnvProperties;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.serializer.Constants;
import com.fobgochod.service.business.ShareService;
import com.fobgochod.service.client.FileInfoCrudService;
import com.fobgochod.service.client.ShareCrudService;
import com.fobgochod.util.SnowFlake;
import com.fobgochod.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private ShareCrudService shareCrudService;
    @Autowired
    private FileInfoCrudService fileInfoCrudService;

    @Override
    public String shareFileForever(FileInfo fileInfo) {
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setId(SnowFlake.getInstance().get());
        shareRecord.setUserId(UserUtil.getUserId());
        shareRecord.setUserName(UserUtil.getUserName());
        shareRecord.setFileId(fileInfo.getId());
        shareRecord.setFileName(fileInfo.getName());
        shareRecord.setUrl(getShareUrl(shareRecord.getId()));
        shareRecord.setExpired(false);
        shareRecord.setFileIds(Collections.singletonList(shareRecord.getFileId()));
        shareCrudService.insert(shareRecord);
        // 操作记录
        fileInfoCrudService.update(fileInfo);
        return shareRecord.getUrl();
    }

    @Override
    public String shareFileTimeLimit(FileInfo fileInfo, int d) {
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setId(SnowFlake.getInstance().get());
        shareRecord.setUserId(UserUtil.getUserId());
        shareRecord.setUserName(UserUtil.getUserName());
        shareRecord.setFileId(fileInfo.getId());
        shareRecord.setFileName(fileInfo.getName());
        shareRecord.setUrl(getShareUrl(shareRecord.getId()));
        shareRecord.setExpired(true);
        shareRecord.setExpireDate(LocalDateTime.now().plusDays(d).format(Constants.DATETIME_FORMATTER));
        shareRecord.setFileIds(Collections.singletonList(shareRecord.getFileId()));
        shareCrudService.insert(shareRecord);
        // 操作记录
        fileInfoCrudService.update(fileInfo);
        return shareRecord.getUrl();
    }

    private String getShareUrl(String shareId) {
        return String.format("%s%s/file/share/%s", envProperties.getBaseUri(), envProperties.getContextPath(), shareId);
    }
}
