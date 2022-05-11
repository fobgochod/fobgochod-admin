package com.fobgochod.domain.medicine;

import com.fobgochod.domain.AbstractConverter;
import com.fobgochod.entity.spda.MedicineItem;

import java.time.LocalTime;

public class MedicineItemVO extends AbstractConverter<MedicineItem> {

    private String type;
    private LocalTime start;
    private LocalTime end;
    private Float slice;
    private Boolean state;

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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
