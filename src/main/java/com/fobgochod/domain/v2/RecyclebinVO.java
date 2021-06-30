package com.fobgochod.domain.v2;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 回收站
 *
 * @author zhouxiao
 * @date 2021/1/18
 */
public class RecyclebinVO {

    private UUID id;
    private String name;
    private String contentType;
    private String paths;
    private LocalDateTime deleteDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
}
