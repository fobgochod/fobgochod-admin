package com.fobgochod.entity.admin;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 药物信息
 *
 * @author Xiao
 * @date 2022/2/10 0:06
 */
@Document("medicine")
public class Medicine extends BaseEntity {

    private String userId;
    private String code;
    private String name;
    private Integer total;
    private Integer morning;
    private Integer noon;
    private Integer night;
    private Integer remain;
    private String remark;
    @Transient
    private List<MedicineRecord> records;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMorning() {
        return morning;
    }

    public void setMorning(Integer morning) {
        this.morning = morning;
    }

    public Integer getNoon() {
        return noon;
    }

    public void setNoon(Integer noon) {
        this.noon = noon;
    }

    public Integer getNight() {
        return night;
    }

    public void setNight(Integer night) {
        this.night = night;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MedicineRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MedicineRecord> records) {
        this.records = records;
    }
}
