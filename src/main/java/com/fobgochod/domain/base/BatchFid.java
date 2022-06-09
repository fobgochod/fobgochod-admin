package com.fobgochod.domain.base;

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
public class BatchFid extends Fid implements InitializingBean {

    private List<String> ids;
    private List<String> fileIds;
    private List<String> dirIds;
    private List<String> recycleIds;
    private LocalDateTime expireDate;

    public BatchFid() {
        this.ids = new ArrayList<>();
        this.fileIds = new ArrayList<>();
        this.dirIds = new ArrayList<>();
        this.recycleIds = new ArrayList<>();
    }

    private static String format(List<String> ids) {
        return ids.stream().map(o -> String.format("\"%s\"", o)).collect(Collectors.joining(",", "[", "]"));
    }

    private static String format(LocalDateTime date) {
        return date == null ? null : String.format("\"%s\"", Constants.DATETIME_FORMATTER.format(date));
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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
        if (recycleIds == null) {
            recycleIds = new ArrayList<>();
        }
        if (this.fileId != null) {
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
        if (recycleId != null) {
            if (!recycleIds.contains(recycleId)) {
                recycleIds.add(recycleId);
            }
            this.recycleId = null;
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"fileId\":"
                + fileId
                + ",\"dirId\":"
                + dirId
                + ",\"recycleId\":"
                + recycleId
                + ",\"fileIds\":"
                + format(fileIds)
                + ",\"dirIds\":"
                + format(dirIds)
                + ",\"recycleIds\":"
                + format(recycleIds)
                + ",\"expireDate\":"
                + format(expireDate)
                + "}";
    }
}
