package com.fobgochod.domain.base;

import java.io.Serializable;

/**
 * ID
 *
 * @author zhouxiao
 * @date 2021/7/13
 */
public class Fid implements Serializable {

    /**
     * 主键
     */
    protected String id;
    /**
     * 文件信息ID
     * 表FileInfo的ID，即fileInfoId
     * 对外统一称为fileId
     * 实际的File的ID不对外暴露
     */
    protected String fileId;
    protected String dirId;
    protected String recycleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getRecycleId() {
        return recycleId;
    }

    public void setRecycleId(String recycleId) {
        this.recycleId = recycleId;
    }
}
