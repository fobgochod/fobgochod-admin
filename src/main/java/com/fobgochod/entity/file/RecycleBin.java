package com.fobgochod.entity.file;

import com.fobgochod.domain.enumeration.FileTypeEnum;
import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 垃圾回收站
 *
 * @author seven
 * @date 2021/6/28
 */
@Document("RecycleBin")
public class RecycleBin extends BaseEntity {

    /**
     * 文件目录ID
     */
    private String fileId;
    /**
     * 文件目录名称
     */
    private String fileName;
    /**
     * 原位置
     */
    private String paths;
    /**
     * 删除时间
     */
    private LocalDateTime deleteDate;
    /**
     * 大小
     */
    private Long size;
    /**
     * 类型：File、Directory
     */
    private FileTypeEnum type;

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

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public LocalDateTime getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(LocalDateTime deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public FileTypeEnum getType() {
        return type;
    }

    public void setType(FileTypeEnum type) {
        this.type = type;
    }
}
