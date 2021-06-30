package com.fobgochod.entity.admin;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Task 文件策略
 *
 * @author zhouxiao
 * @date 2020/10/30
 */
@Document("task")
public class Task extends BaseEntity {

    private String code;
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * cron表达式
     */
    private String cron;
    private String className;
    /**
     * 是否启用
     */
    private Boolean disable;
    /**
     * 自定义，对于邮件 定义事件ID
     */
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
