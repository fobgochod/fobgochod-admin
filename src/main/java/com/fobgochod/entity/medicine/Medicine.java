package com.fobgochod.entity.medicine;

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

    private String userCode;
    private String code;
    private String name;
    private Integer slice;
    private Float total;
    private Integer remain;
    private Boolean status;
    private String remark;
    @Transient
    private List<MedicineRecord> records;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    public Integer getSlice() {
        return slice;
    }

    public void setSlice(Integer slice) {
        this.slice = slice;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
