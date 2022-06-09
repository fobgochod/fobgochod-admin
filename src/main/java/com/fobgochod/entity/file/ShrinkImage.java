package com.fobgochod.entity.file;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 原图和缩略图关系(FileInfo的id关联)
 *
 * @author zhouxiao
 * @date 2020/5/21
 */
@Document("ShrinkImage")
public class ShrinkImage extends BaseEntity {

    /**
     * 原图文件ID
     */
    private String fileId;
    /**
     * 压缩后的图片文件ID
     */
    private String shrinkId;
    private Integer width;
    private Integer height;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getShrinkId() {
        return shrinkId;
    }

    public void setShrinkId(String shrinkId) {
        this.shrinkId = shrinkId;
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
}
