package com.fobgochod.entity.file;

import com.fobgochod.entity.BaseEntity;
import org.bson.Document;

/**
 * 原图和缩略图关系(FileInfo的id关联)
 *
 * @author zhouxiao
 * @date 2020/5/21
 */
@org.springframework.data.mongodb.core.mapping.Document("ShrinkImage")
public class ShrinkImage extends BaseEntity {

    private String sourceId;
    private Document property;
    private String targetId;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Document getProperty() {
        return property;
    }

    public void setProperty(Document property) {
        this.property = property;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
