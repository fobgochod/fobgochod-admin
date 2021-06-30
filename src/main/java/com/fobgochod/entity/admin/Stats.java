package com.fobgochod.entity.admin;

import com.fobgochod.domain.v2.BucketStats;
import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Bucket使用情况
 *
 * @author zhouxiao
 * @date 2020/11/19
 */
@Document("stats")
public class Stats extends BaseEntity {

    private Integer year;
    private Integer month;
    private Integer bucketCount;
    private Integer userCount;
    private Long fileCount;
    private Long totalSize;
    private String totalSizeReadable;
    private List<BucketStats> buckets;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getBucketCount() {
        return bucketCount;
    }

    public void setBucketCount(Integer bucketCount) {
        this.bucketCount = bucketCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Long getFileCount() {
        return fileCount;
    }

    public void setFileCount(Long fileCount) {
        this.fileCount = fileCount;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getTotalSizeReadable() {
        return totalSizeReadable;
    }

    public void setTotalSizeReadable(String totalSizeReadable) {
        this.totalSizeReadable = totalSizeReadable;
    }

    public List<BucketStats> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<BucketStats> buckets) {
        this.buckets = buckets;
    }
}
