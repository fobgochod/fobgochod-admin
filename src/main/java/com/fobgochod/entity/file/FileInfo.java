package com.fobgochod.entity.file;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.entity.BaseEntity;
import com.fobgochod.exception.SystemException;
import com.fobgochod.util.JsonUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 文件信息
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
@org.springframework.data.mongodb.core.mapping.Document("FileInfo")
public class FileInfo extends BaseEntity {

    private String name;
    private String displayName;
    private String tag;
    private long size;
    private String extension;
    private String contentType;
    private boolean completed;
    private LocalDateTime expireDate;
    private Document metadata = new Document();
    private String directoryId;
    private ObjectId fileId;

    public static FileInfo get(String fileJson) {
        try {
            FileInfo fileInfo = JsonUtils.createObjectMapper()
                    .readValue(URLDecoder.decode(fileJson, StandardCharsets.UTF_8.name()), FileInfo.class);
            if (StringUtils.isEmpty(fileInfo.name)) {
                throw new SystemException(I18nCode.ERROR_10001);
            }
            return fileInfo;
        } catch (Exception e) {
            throw new SystemException(I18nCode.ERROR_10001);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public Document getMetadata() {
        return metadata;
    }

    public void setMetadata(Document metadata) {
        this.metadata = metadata;
    }

    public String getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    @Override
    public FileInfo clone() {
        try {
            return (FileInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
