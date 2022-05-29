package com.fobgochod.domain;

import org.springframework.data.annotation.Id;

public class StatsResult {

    @Id
    private String tenantId;
    private long size;
    private long count;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
