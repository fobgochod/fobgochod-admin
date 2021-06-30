package com.fobgochod.entity.file;

import com.fobgochod.domain.DirTree;
import com.fobgochod.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 垃圾回收站
 *
 * @author seven
 * @date 2021/6/28
 */
@Document("RecycleBin")
public class RecycleBin extends BaseEntity {

    /**
     * 文件名称
     */
    private String name;
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
    private long size;
    /**
     * 类型：File、Directory
     */
    private String contentType;
    @JsonIgnore
    private FileInfo fileInfo;
    @JsonIgnore
    private DirTree dirInfo;
    @JsonIgnore
    private List<DirInfo> dirInfos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public DirTree getDirInfo() {
        return dirInfo;
    }

    public void setDirInfo(DirTree dirTree) {
        this.dirInfo = dirTree;
    }

    public List<DirInfo> getDirInfos() {
        return dirInfos;
    }

    public void setDirInfos(List<DirInfo> dirInfos) {
        this.dirInfos = dirInfos;
    }
}
