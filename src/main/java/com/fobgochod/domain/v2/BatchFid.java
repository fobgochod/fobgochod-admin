package com.fobgochod.domain.v2;

import com.fobgochod.entity.file.ShareRecord;
import com.fobgochod.serializer.Constants;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量操作文件
 *
 * @author zhouxiao
 * @date 2021/2/23
 */
public class BatchFid implements InitializingBean {

    private String id;
    private List<String> ids;
    /**
     * 文件信息ID
     * 表FileInfo的ID，即fileInfoId
     * 对外统一称为fileId
     * 实际的File的ID不对外暴露
     */
    private String fileId;
    /**
     * 目录ID
     */
    private String dirId;
    /**
     * 文件ID集合
     */
    private List<String> fileIds;
    /**
     * 目录ID集合
     */
    private List<String> dirIds;
    /**
     * 分享ID集合
     */
    private List<String> shareIds;
    /**
     * 回收站ID集合
     */
    private List<String> recycleIds;
    /**
     * 过期时间
     */
    private LocalDateTime expireDate;

    public BatchFid() {
        this.ids = new ArrayList<>();
        this.fileIds = new ArrayList<>();
        this.dirIds = new ArrayList<>();
        this.shareIds = new ArrayList<>();
        this.recycleIds = new ArrayList<>();
    }

    public BatchFid(ShareRecord shareRecord) {
        this();
        if (shareRecord != null) {
            this.fileIds = shareRecord.getFileIds();
            this.dirIds = shareRecord.getDirIds();
        }
    }

    private static String format(List<String> ids) {
        return ids.stream().map(o -> String.format("\"%s\"", o)).collect(Collectors.joining(",", "[", "]"));
    }

    private static String format(LocalDateTime date) {
        return date == null ? null : String.format("\"%s\"", Constants.DATETIME_FORMATTER.format(date));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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

    public List<String> getShareIds() {
        return shareIds;
    }

    public void setShareIds(List<String> shareIds) {
        this.shareIds = shareIds;
    }

    public List<String> getRecycleIds() {
        return recycleIds;
    }

    public void setRecycleIds(List<String> recycleIds) {
        this.recycleIds = recycleIds;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public void afterPropertiesSet() {
        if (fileIds == null) {
            fileIds = new ArrayList<>();
        }
        if (dirIds == null) {
            dirIds = new ArrayList<>();
        }
        if (fileId != null) {
            if (!fileIds.contains(fileId)) {
                fileIds.add(fileId);
            }
            this.fileId = null;
        }
        if (dirId != null) {
            if (!dirIds.contains(dirId)) {
                dirIds.add(dirId);
            }
            this.dirId = null;
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"fileId\":"
                + fileId
                + ",\"dirId\":"
                + dirId
                + ",\"fileIds\":"
                + format(fileIds)
                + ",\"dirIds\":"
                + format(dirIds)
                + ",\"shareIds\":"
                + format(shareIds)
                + ",\"recycleIds\":"
                + format(recycleIds)
                + ",\"expireDate\":"
                + format(expireDate)
                + "}";
    }
}
