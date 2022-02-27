package com.fobgochod.entity.admin;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 药物使用记录
 *
 * @author Xiao
 * @date 2022/2/21 21:51
 */
@Document("medicine_record")
public class MedicineRecord extends BaseEntity {

    private String medicineId;
    private String type;
    private Float slice;
    private LocalDateTime time;

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getSlice() {
        return slice;
    }

    public void setSlice(Float slice) {
        this.slice = slice;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
