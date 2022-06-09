package com.fobgochod.entity.file;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.entity.BaseEntity;
import com.fobgochod.exception.SystemException;
import com.fobgochod.util.JsonUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ObjectUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件信息
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
@Document("FileInfo")
public class FileInfo extends BaseEntity {

    private String name;
    private Long size;
    private String suffix;
    private String mediaType;
    private Integer width;
    private Integer height;
    private String directoryId;
    private String tag;
    private ObjectId fileId;
    private Boolean completed;

    public static FileInfo get(String fileJson) {
        try {
            FileInfo fileInfo = JsonUtils.createObjectMapper()
                    .readValue(URLDecoder.decode(fileJson, StandardCharsets.UTF_8.name()), FileInfo.class);
            if (ObjectUtils.isEmpty(fileInfo.name)) {
                throw new SystemException(I18nCode.FILE_NAME_NONE);
            }
            return fileInfo;
        } catch (Exception e) {
            throw new SystemException(I18nCode.FILE_NAME_NONE);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
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
