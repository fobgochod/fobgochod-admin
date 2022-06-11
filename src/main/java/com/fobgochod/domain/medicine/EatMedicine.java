package com.fobgochod.domain.medicine;

import java.time.LocalDate;

public class EatMedicine {

    private String userCode;
    private LocalDate date;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
