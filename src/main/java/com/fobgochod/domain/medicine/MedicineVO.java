package com.fobgochod.domain.medicine;

import com.fobgochod.domain.AbstractConverter;
import com.fobgochod.entity.admin.Medicine;

public class MedicineVO extends AbstractConverter<Medicine> {

    private String code;
    private String name;
    private Float total;
    private Float morning;
    private Float noon;
    private Float night;
    private Integer remain;

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
}
