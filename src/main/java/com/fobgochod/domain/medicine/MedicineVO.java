package com.fobgochod.domain.medicine;

import com.fobgochod.domain.AbstractConverter;
import com.fobgochod.entity.medicine.Medicine;

import java.util.List;

public class MedicineVO extends AbstractConverter<Medicine> {

    private String code;
    private String name;
    private Integer slice;
    private Float total;
    private Integer remain;
    private List<MedicineItemVO> items;

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

    public List<MedicineItemVO> getItems() {
        return items;
    }

    public void setItems(List<MedicineItemVO> items) {
        this.items = items;
    }
}
