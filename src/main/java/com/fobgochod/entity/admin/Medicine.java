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
    private Float total;
    private Float morning;
    private Float noon;
    private Float night;
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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getMorning() {
        return morning;
    }

    public void setMorning(Float morning) {
        this.morning = morning;
    }

    public Float getNoon() {
        return noon;
    }

    public void setNoon(Float noon) {
        this.noon = noon;
    }

    public Float getNight() {
        return night;
    }

    public void setNight(Float night) {
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
