package com.fobgochod.domain.medicine;

import com.fobgochod.domain.AbstractConverter;
import com.fobgochod.entity.admin.Medicine;

public class MedicineVO extends AbstractConverter<Medicine> {

    private String code;
    private String name;
    private Integer total;
    private Integer morning;
    private Integer noon;
    private Integer night;
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
}
