package com.fobgochod.entity.medicine;

import com.fobgochod.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

/**
 * 吃药明细
 *
 * @author Xiao
 * @date 2022/5/10 22:22
 */
@Document("medicine_item")
public class MedicineItem extends BaseEntity {

    private String medicineId;
    private String type;
    private LocalTime start;
    private LocalTime end;
    private Float slice;

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

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public Float getSlice() {
        return slice;
    }

    public void setSlice(Float slice) {
        this.slice = slice;
    }
}
