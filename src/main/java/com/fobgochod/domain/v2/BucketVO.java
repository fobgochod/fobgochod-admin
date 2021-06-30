package com.fobgochod.domain.v2;

/**
 * Bucket初始化
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
public class BucketVO {

    /**
     * BucketName
     */
    private String bucket;
    /**
     * Bucket描述
     */
    private String description;
    /**
     * Bucket所有者
     */
    private String owner;
    /**
     * Bucket所有者密码
     */
    private String password;
    /**
     * 文件管理删除策略
     */
    private String task;
    private String apiUrl;
    private String versionNum;
    private String tenantId;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
