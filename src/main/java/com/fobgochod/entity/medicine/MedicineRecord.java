package com.fobgochod.entity.medicine;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 药物使用记录
 *
 * @author Xiao
 * @date 2022/2/21 21:51
 */
@Document("medicine_record")
public class MedicineRecord extends BaseEntity {

    private String userId;
    private String medicineId;
    private String type;
    private Float slice;
    private LocalDate date;
    private LocalTime time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
