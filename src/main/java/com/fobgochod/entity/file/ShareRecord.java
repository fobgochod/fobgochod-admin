package com.fobgochod.entity.file;

import com.fobgochod.domain.enumeration.ShareType;
import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享记录
 *
 * @author seven
 * @date 2021/6/28
 */
@Document("ShareRecord")
public class ShareRecord extends BaseEntity {

    private ShareType type = ShareType.One;
    /**
     * 分享者id
     */
    private String userId;
    private String userName;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 分享文件的名称
     */
    private String fileName;
    /**
     * 分享地址
     */
    private String url;
    /**
     * 过期时间
     */
    private LocalDateTime expireDate;
    /**
     * 分享的文件、目录
     */
    private List<String> fileIds;
    private List<String> dirIds;

    public ShareRecord() {
        this.fileIds = new ArrayList<>();
        this.dirIds = new ArrayList<>();
    }

    public ShareType getType() {
        return type;
    }

    public void setType(ShareType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public List<String> getDirIds() {
        return dirIds;
    }

    public void setDirIds(List<String> dirIds) {
        this.dirIds = dirIds;
    }
}
