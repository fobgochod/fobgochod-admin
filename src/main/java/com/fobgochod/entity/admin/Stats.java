package com.fobgochod.entity.admin;

import com.fobgochod.domain.StatsResult;
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

    private String year;
    private String month;
    private Integer userCount;
    private Integer tenantCount;
    private Long fileCount;
    private Long totalSize;
    private List<StatsResult> tenants;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getTenantCount() {
        return tenantCount;
    }

    public void setTenantCount(Integer tenantCount) {
        this.tenantCount = tenantCount;
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

    public List<StatsResult> getTenants() {
        return tenants;
    }

    public void setTenants(List<StatsResult> tenants) {
        this.tenants = tenants;
    }
}
